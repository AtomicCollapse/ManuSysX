package top.hondaman.manux.module.im.service.groupmessage;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.hondaman.manux.framework.security.core.util.SecurityFrameworkUtils;
import top.hondaman.manux.module.im.api.groupmessage.dto.GroupMessageDTO;
import top.hondaman.manux.module.im.controller.admin.groupmessage.vo.GroupMessageVO;
import top.hondaman.manux.module.im.dal.dataobject.group.GroupDO;
import top.hondaman.manux.module.im.dal.dataobject.groupmember.GroupMemberDO;
import top.hondaman.manux.module.im.dal.dataobject.groupmessage.GroupMessageDO;
import top.hondaman.manux.module.im.dal.mysql.groupmessage.GroupMessageMapper;
import top.hondaman.manux.module.im.enums.*;
import top.hondaman.manux.module.im.framework.im.core.IMClient;
import top.hondaman.manux.module.im.framework.im.model.IMGroupMessage;
import top.hondaman.manux.module.im.framework.im.model.IMUserInfo;
import top.hondaman.manux.module.im.service.group.GroupService;
import top.hondaman.manux.module.im.service.groupmember.GroupMemberService;
import top.hondaman.manux.module.im.util.CommaTextUtils;
import top.hondaman.manux.module.im.util.SensitiveFilterUtil;
import top.hondaman.manux.module.im.util.ThreadPoolExecutorFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.stream.Collectors;

import static top.hondaman.manux.framework.common.exception.util.ServiceExceptionUtil.exception;
import static top.hondaman.manux.module.im.enums.ErrorCodeConstants.*;

@Slf4j
@Service
public class GroupMessageServiceImpl extends ServiceImpl<GroupMessageMapper, GroupMessageDO> implements GroupMessageService {
    @Resource
    private GroupService groupService;
    @Resource
    private GroupMemberService groupMemberService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private IMClient imClient;
    @Resource
    private SensitiveFilterUtil sensitiveFilterUtil;
    private static final ScheduledThreadPoolExecutor EXECUTOR = ThreadPoolExecutorFactory.getThreadPoolExecutor();

    @Override
    public GroupMessageVO sendMessage(GroupMessageDTO dto) {
        GroupDO group = groupService.getAndCheckById(dto.getGroupId());
        // 是否在群聊里面
        GroupMemberDO member = groupMemberService.findByGroupAndUserId(dto.getGroupId(), SecurityFrameworkUtils.getLoginImUserId());
        if (Objects.isNull(member) || member.getQuit()) {
            throw exception(NOT_IN_GROUP);
        }
        // 群聊成员列表
        List<Long> userIds = groupMemberService.findUserIdsByGroupId(group.getId());
        if (dto.getReceipt() && userIds.size() > IMPlatformConstant.MAX_NORMAL_GROUP_MEMBER) {
            // 大群的回执消息过于消耗资源，不允许发送
            throw exception(GROUP_SIZE_OVER_RECEIPT,IMPlatformConstant.MAX_NORMAL_GROUP_MEMBER);
        }
        // 不用发给自己
        userIds = userIds.stream().filter(id -> !SecurityFrameworkUtils.getLoginImUserId().equals(id)).collect(Collectors.toList());
        // 保存消息
        GroupMessageDO msg = BeanUtil.copyProperties(dto, GroupMessageDO.class);
        msg.setSendId(SecurityFrameworkUtils.getLoginImUserId());
        msg.setSendTime(LocalDateTime.now());
        msg.setSendNickName(member.getShowNickName());
        msg.setAtUserIds(CommaTextUtils.asText(dto.getAtUserIds()));
        msg.setStatus(MessageStatus.PENDING.code());
        // 过滤内容中的敏感词
        if (MessageType.TEXT.code().equals(dto.getType())) {
            msg.setContent(sensitiveFilterUtil.filter(dto.getContent()));
        }
        this.save(msg);
        // 群发
        GroupMessageVO msgInfo = BeanUtil.copyProperties(msg, GroupMessageVO.class);
        msgInfo.setAtUserIds(dto.getAtUserIds());
        IMGroupMessage<GroupMessageVO> sendMessage = new IMGroupMessage<>();
        sendMessage.setSender(new IMUserInfo(SecurityFrameworkUtils.getLoginImUserId(), SecurityFrameworkUtils.getLoginTerminate()));
        sendMessage.setRecvIds(userIds);
        sendMessage.setSendResult(false);
        sendMessage.setData(msgInfo);
        imClient.sendGroupMessage(sendMessage);
        log.info("发送群聊消息，发送id:{},群聊id:{},内容:{}", SecurityFrameworkUtils.getLoginImUserId(), dto.getGroupId(), dto.getContent());
        return msgInfo;
    }

    @Transactional
    @Override
    public GroupMessageVO recallMessage(Long id) {
        GroupMessageDO msg = this.getById(id);
        if (Objects.isNull(msg)) {
            throw exception(GROUP_MESSAGE_NOT_EXISTS);
        }
        if (!msg.getSendId().equals(SecurityFrameworkUtils.getLoginImUserId())) {
            throw exception(GROUP_MESSAGE_RECALL_NOT_SELF);
        }
        if (Duration.between(LocalDateTime.now(),msg.getSendTime()).getSeconds() > IMConstant.ALLOW_RECALL_SECOND) {
            throw exception(GROUP_MESSAGE_RECALL_OVER_TIME);
        }
        // 判断是否在群里
        GroupMemberDO member = groupMemberService.findByGroupAndUserId(msg.getGroupId(), SecurityFrameworkUtils.getLoginImUserId());
        if (Objects.isNull(member) || Boolean.TRUE.equals(member.getQuit())) {
            throw exception(GROUP_MESSAGE_RECALL_NOT_IN_GROUP);
        }
        // 修改数据库
        msg.setStatus(MessageStatus.RECALL.code());
        this.updateById(msg);
        // 生成一条撤回消息
        GroupMessageDO recallMsg = new GroupMessageDO();
        recallMsg.setStatus(MessageStatus.PENDING.code());
        recallMsg.setType(MessageType.RECALL.code());
        recallMsg.setGroupId(msg.getGroupId());
        recallMsg.setSendId(SecurityFrameworkUtils.getLoginImUserId());
        recallMsg.setSendNickName(member.getShowNickName());
        recallMsg.setContent(id.toString());
        recallMsg.setSendTime(LocalDateTime.now());
        this.save(recallMsg);
        // 群发
        List<Long> userIds = groupMemberService.findUserIdsByGroupId(msg.getGroupId());
        GroupMessageVO msgInfo = BeanUtil.copyProperties(recallMsg, GroupMessageVO.class);
        IMGroupMessage<GroupMessageVO> sendMessage = new IMGroupMessage<>();
        sendMessage.setSender(new IMUserInfo(SecurityFrameworkUtils.getLoginImUserId(), SecurityFrameworkUtils.getLoginTerminate()));
        sendMessage.setRecvIds(userIds);
        sendMessage.setData(msgInfo);
        imClient.sendGroupMessage(sendMessage);
        log.info("撤回群聊消息，发送id:{},群聊id:{},内容:{}", SecurityFrameworkUtils.getLoginImUserId(), msg.getGroupId(), msg.getContent());
        return msgInfo;
    }

    @Override
    public void pullOfflineMessage(Long minId) {
        if (!imClient.isOnline(SecurityFrameworkUtils.getLoginImUserId())) {
            throw exception(GROUP_MESSAGE_PULL_OFF_LINE);
        }
        // 查询用户加入的群组
        List<GroupMemberDO> members = groupMemberService.findByUserId(SecurityFrameworkUtils.getLoginImUserId());
        Map<Long, GroupMemberDO> groupMemberMap = CollStreamUtil.toIdentityMap(members, GroupMemberDO::getGroupId);
        Set<Long> groupIds = groupMemberMap.keySet();
        if (CollectionUtil.isEmpty(groupIds)) {
            // 关闭加载中标志
            this.sendLoadingMessage(false);
            return;
        }
        // 只拉最近一个月
        Date minDate = DateUtils.addMonths(new Date(), -1);
        LambdaQueryWrapper<GroupMessageDO> wrapper = Wrappers.lambdaQuery();
        wrapper.gt(GroupMessageDO::getId, minId);
        wrapper.gt(GroupMessageDO::getSendTime, minDate);
        wrapper.in(GroupMessageDO::getGroupId, groupIds);
        wrapper.orderByDesc(GroupMessageDO::getId);
        wrapper.last("limit 50000");
        List<GroupMessageDO> messages = this.list(wrapper);
        // 通过群聊对消息进行分组
        Map<Long, List<GroupMessageDO>> messageGroupMap =
            messages.stream().collect(Collectors.groupingBy(GroupMessageDO::getGroupId));
        // 退群前的消息
        List<GroupMemberDO> quitMembers = groupMemberService.findQuitInMonth(SecurityFrameworkUtils.getLoginImUserId());
        for (GroupMemberDO quitMember : quitMembers) {
            wrapper = Wrappers.lambdaQuery();
            wrapper.gt(GroupMessageDO::getId, minId);
            wrapper.between(GroupMessageDO::getSendTime, minDate, quitMember.getQuitTime());
            wrapper.eq(GroupMessageDO::getGroupId, quitMember.getGroupId());
            wrapper.ne(GroupMessageDO::getStatus, MessageStatus.RECALL.code());
            wrapper.orderByDesc(GroupMessageDO::getId);
            List<GroupMessageDO> groupMessages = this.list(wrapper);
            messageGroupMap.put(quitMember.getGroupId(), groupMessages);
            groupMemberMap.put(quitMember.getGroupId(), quitMember);
        }
        EXECUTOR.execute(() -> {
            // 开启加载中标志
            this.sendLoadingMessage(true);
            // 推送消息
            int sendCount = 0;
            for (Map.Entry<Long, List<GroupMessageDO>> entry : messageGroupMap.entrySet()) {
                Long groupId = entry.getKey();
                List<GroupMessageDO> groupMessages = entry.getValue();
                // 第一次拉取时,一个群最多推送3000条消息，防止前端接收能力溢出导致卡顿
                List<GroupMessageDO> sendMessages = groupMessages;
                if (minId <= 0 && groupMessages.size() > 3000) {
                    sendMessages = groupMessages.subList(0, 3000);
                }
                // id从小到大排序
                CollectionUtil.reverse(sendMessages);
                // 填充消息状态
                String key = StrUtil.join(":", IMPlatformRedisKey.IM_GROUP_READED_POSITION, groupId);
                Object o = redisTemplate.opsForHash().get(key, SecurityFrameworkUtils.getLoginImUserId().toString());
                long readedMaxId = Objects.isNull(o) ? -1 : Long.parseLong(o.toString());
                Map<Object, Object> maxIdMap = null;
                for (GroupMessageDO m : sendMessages) {
                    // 推送过程如果用户下线了，则不再推送
                    if (!imClient.isOnline(SecurityFrameworkUtils.getLoginImUserId(), IMTerminalType.fromCode(SecurityFrameworkUtils.getLoginTerminate()))) {
                        log.info("用户已下线，停止推送离线群聊消息,用户id:{}", SecurityFrameworkUtils.getLoginImUserId());
                        return;
                    }
                    // 排除加群之前的消息
                    GroupMemberDO member = groupMemberMap.get(m.getGroupId());
                    if (member.getCreateTime().isAfter(m.getSendTime())) {
                        continue;
                    }
                    // 排除不需要接收的消息
                    List<String> recvIds = CommaTextUtils.asList(m.getRecvIds());
                    if (!recvIds.isEmpty() && !recvIds.contains(SecurityFrameworkUtils.getLoginImUserId().toString())) {
                        continue;
                    }
                    // 组装vo
                    GroupMessageVO vo = BeanUtil.copyProperties(m, GroupMessageVO.class);
                    // 被@用户列表
                    List<String> atIds = CommaTextUtils.asList(m.getAtUserIds());
                    vo.setAtUserIds(atIds.stream().map(Long::parseLong).collect(Collectors.toList()));
                    // 填充状态
                    vo.setStatus(readedMaxId >= m.getId() ? MessageStatus.READED.code() : MessageStatus.PENDING.code());
                    // 针对回执消息填充已读人数
                    if (m.getReceipt()) {
                        if (Objects.isNull(maxIdMap)) {
                            maxIdMap = redisTemplate.opsForHash().entries(key);
                        }
                        int count = getReadedUserIds(maxIdMap, m.getId(), m.getSendId()).size();
                        vo.setReadedCount(count);
                    }
                    // 推送
                    IMGroupMessage<GroupMessageVO> sendMessage = new IMGroupMessage<>();
                    sendMessage.setSender(new IMUserInfo(m.getSendId(), IMTerminalType.WEB.code()));
                    sendMessage.setRecvIds(Arrays.asList(SecurityFrameworkUtils.getLoginImUserId()));
                    sendMessage.setRecvTerminals(Arrays.asList(SecurityFrameworkUtils.getLoginTerminate()));
                    sendMessage.setSendResult(false);
                    sendMessage.setSendToSelf(false);
                    sendMessage.setData(vo);
                    imClient.sendGroupMessage(sendMessage);
                    sendCount++;
                }
            }
            // 关闭加载中标志
            this.sendLoadingMessage(false);
            log.info("拉取离线群聊消息,用户id:{},数量:{}", SecurityFrameworkUtils.getLoginImUserId(), sendCount++);
        });
    }

    @Override
    public List<GroupMessageVO> loadOffineMessage(Long minId) {
        // 查询用户加入的群组
        List<GroupMemberDO> members = groupMemberService.findByUserId(SecurityFrameworkUtils.getLoginImUserId());
        Map<Long, GroupMemberDO> groupMemberMap = CollStreamUtil.toIdentityMap(members, GroupMemberDO::getGroupId);
        Set<Long> groupIds = groupMemberMap.keySet();
        if (groupIds.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        // 只能拉取最近1个月的消息
        Date minDate = DateUtils.addMonths(new Date(), -1);
        LambdaQueryWrapper<GroupMessageDO> wrapper = Wrappers.lambdaQuery();
        wrapper.gt(GroupMessageDO::getId, minId);
        wrapper.gt(GroupMessageDO::getSendTime, minDate);
        wrapper.in(GroupMessageDO::getGroupId, groupIds);
        wrapper.orderByDesc(GroupMessageDO::getId);
        wrapper.last("limit 50000");
        List<GroupMessageDO> messages = this.list(wrapper);
        // 退群前的消息
        List<GroupMemberDO> quitMembers = groupMemberService.findQuitInMonth(SecurityFrameworkUtils.getLoginImUserId());
        for (GroupMemberDO quitMember : quitMembers) {
            wrapper = Wrappers.lambdaQuery();
            wrapper.gt(GroupMessageDO::getId, minId);
            wrapper.between(GroupMessageDO::getSendTime, minDate, quitMember.getQuitTime());
            wrapper.eq(GroupMessageDO::getGroupId, quitMember.getGroupId());
            wrapper.orderByDesc(GroupMessageDO::getId);
            wrapper.last("limit 1000");
            List<GroupMessageDO> groupMessages = this.list(wrapper);
            if (!groupMessages.isEmpty()) {
                messages.addAll(groupMessages);
                groupMemberMap.put(quitMember.getGroupId(), quitMember);
            }
        }
        // 通过群聊对消息进行分组
        Map<Long, List<GroupMessageDO>> messageGroupMap =
            messages.stream().collect(Collectors.groupingBy(GroupMessageDO::getGroupId));
        List<GroupMessageVO> vos = new LinkedList<>();
        for (Map.Entry<Long, List<GroupMessageDO>> entry : messageGroupMap.entrySet()) {
            Long groupId = entry.getKey();
            List<GroupMessageDO> groupMessages = entry.getValue();
            // 填充消息状态
            String key = StrUtil.join(":", IMPlatformRedisKey.IM_GROUP_READED_POSITION, groupId);
            Object o = redisTemplate.opsForHash().get(key, SecurityFrameworkUtils.getLoginImUserId().toString());
            long readedMaxId = Objects.isNull(o) ? -1 : Long.parseLong(o.toString());
            Map<Object, Object> maxIdMap = null;
            for (GroupMessageDO m : groupMessages) {
                // 排除加群之前的消息
                GroupMemberDO member = groupMemberMap.get(m.getGroupId());
                if (member.getCreateTime().isAfter(m.getSendTime())) {
                    continue;
                }
                // 排除不需要接收的消息
                List<String> recvIds = CommaTextUtils.asList(m.getRecvIds());
                if (!recvIds.isEmpty() && !recvIds.contains(SecurityFrameworkUtils.getLoginImUserId().toString())) {
                    continue;
                }
                // 组装vo
                GroupMessageVO vo = BeanUtil.copyProperties(m, GroupMessageVO.class);
                // 被@用户列表
                List<String> atIds = CommaTextUtils.asList(m.getAtUserIds());
                vo.setAtUserIds(atIds.stream().map(Long::parseLong).collect(Collectors.toList()));
                // 填充状态
                vo.setStatus(readedMaxId >= m.getId() ? MessageStatus.READED.code() : MessageStatus.PENDING.code());
                // 针对回执消息填充已读人数
                if (m.getReceipt()) {
                    if (Objects.isNull(maxIdMap)) {
                        maxIdMap = redisTemplate.opsForHash().entries(key);
                    }
                    int count = getReadedUserIds(maxIdMap, m.getId(), m.getSendId()).size();
                    vo.setReadedCount(count);
                }
                vos.add(vo);
            }
        }
        // 排序
        return vos.stream().sorted(Comparator.comparing(GroupMessageVO::getId)).collect(Collectors.toList());
    }


    @Override
    public void readedMessage(Long groupId) {
        // 取出最后的消息id
        LambdaQueryWrapper<GroupMessageDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(GroupMessageDO::getGroupId, groupId).orderByDesc(GroupMessageDO::getId).last("limit 1")
            .select(GroupMessageDO::getId);
        GroupMessageDO message = this.getOne(wrapper);
        if (Objects.isNull(message)) {
            return;
        }
        // 推送消息给自己的其他终端,同步清空会话列表中的未读数量
        GroupMessageVO msgInfo = new GroupMessageVO();
        msgInfo.setType(MessageType.READED.code());
        msgInfo.setSendTime(LocalDateTime.now());
        msgInfo.setSendId(SecurityFrameworkUtils.getLoginImUserId());
        msgInfo.setGroupId(groupId);
        IMGroupMessage<GroupMessageVO> sendMessage = new IMGroupMessage<>();
        sendMessage.setSender(new IMUserInfo(SecurityFrameworkUtils.getLoginImUserId(), SecurityFrameworkUtils.getLoginTerminate()));
        sendMessage.setSendToSelf(true);
        sendMessage.setData(msgInfo);
        sendMessage.setSendResult(true);
        imClient.sendGroupMessage(sendMessage);
        // 已读消息key
        String key = StrUtil.join(":", IMPlatformRedisKey.IM_GROUP_READED_POSITION, groupId);
        // 原来的已读消息位置
        Object maxReadedId = redisTemplate.opsForHash().get(key, SecurityFrameworkUtils.getLoginImUserId().toString());
        // 记录已读消息位置
        redisTemplate.opsForHash().put(key, SecurityFrameworkUtils.getLoginImUserId().toString(), message.getId());
        // 推送消息回执，刷新已读人数显示
        wrapper = Wrappers.lambdaQuery();
        wrapper.eq(GroupMessageDO::getGroupId, groupId);
        wrapper.gt(!Objects.isNull(maxReadedId), GroupMessageDO::getId, maxReadedId);
        wrapper.le(!Objects.isNull(maxReadedId), GroupMessageDO::getId, message.getId());
        wrapper.ne(GroupMessageDO::getStatus, MessageStatus.RECALL.code());
        wrapper.eq(GroupMessageDO::getReceipt, true);
        List<GroupMessageDO> receiptMessages = this.list(wrapper);
        if (CollectionUtil.isNotEmpty(receiptMessages)) {
            List<Long> userIds = groupMemberService.findUserIdsByGroupId(groupId);
            Map<Object, Object> maxIdMap = redisTemplate.opsForHash().entries(key);
            for (GroupMessageDO receiptMessage : receiptMessages) {
                Integer readedCount =
                    getReadedUserIds(maxIdMap, receiptMessage.getId(), receiptMessage.getSendId()).size();
                // 如果所有人都已读，记录回执消息完成标记
                if (readedCount >= userIds.size() - 1) {
                    receiptMessage.setReceiptOk(true);
                    this.updateById(receiptMessage);
                }
                msgInfo = new GroupMessageVO();
                msgInfo.setId(receiptMessage.getId());
                msgInfo.setGroupId(groupId);
                msgInfo.setReadedCount(readedCount);
                msgInfo.setReceiptOk(receiptMessage.getReceiptOk());
                msgInfo.setType(MessageType.RECEIPT.code());
                sendMessage = new IMGroupMessage<>();
                sendMessage.setSender(new IMUserInfo(SecurityFrameworkUtils.getLoginImUserId(), SecurityFrameworkUtils.getLoginTerminate()));
                sendMessage.setRecvIds(userIds);
                sendMessage.setData(msgInfo);
                sendMessage.setSendToSelf(false);
                sendMessage.setSendResult(false);
                imClient.sendGroupMessage(sendMessage);
            }
        }
    }

    @Override
    public List<Long> findReadedUsers(Long groupId, Long messageId) {
        GroupMessageDO message = this.getById(messageId);
        if (Objects.isNull(message)) {
            throw exception(GROUP_MESSAGE_NOT_EXISTS);
        }
        // 是否在群聊里面
        GroupMemberDO member = groupMemberService.findByGroupAndUserId(groupId, SecurityFrameworkUtils.getLoginImUserId());
        if (Objects.isNull(member) || member.getQuit()) {
            throw exception(NOT_IN_GROUP);
        }
        // 已读位置key
        String key = StrUtil.join(":", IMPlatformRedisKey.IM_GROUP_READED_POSITION, groupId);
        // 一次获取所有用户的已读位置
        Map<Object, Object> maxIdMap = redisTemplate.opsForHash().entries(key);
        // 返回已读用户的id集合
        return getReadedUserIds(maxIdMap, message.getId(), message.getSendId());
    }

    @Override
    public List<GroupMessageVO> findHistoryMessage(Long groupId, Long page, Long size) {
        page = page > 0 ? page : 1;
        size = size > 0 ? size : 10;
        Long userId = SecurityFrameworkUtils.getLoginImUserId();
        long stIdx = (page - 1) * size;
        // 群聊成员信息
        GroupMemberDO member = groupMemberService.findByGroupAndUserId(groupId, userId);
        if (Objects.isNull(member) || member.getQuit()) {
            throw exception(NOT_IN_GROUP);
        }
        // 查询聊天记录，只查询加入群聊时间之后的消息
        QueryWrapper<GroupMessageDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GroupMessageDO::getGroupId, groupId).gt(GroupMessageDO::getSendTime, member.getCreateTime())
            .ne(GroupMessageDO::getStatus, MessageStatus.RECALL.code()).orderByDesc(GroupMessageDO::getId)
            .last("limit " + stIdx + "," + size);
        List<GroupMessageDO> messages = this.list(wrapper);
        List<GroupMessageVO> messageInfos =
            messages.stream().map(m -> BeanUtil.copyProperties(m, GroupMessageVO.class)).collect(Collectors.toList());
        log.info("拉取群聊记录，用户id:{},群聊id:{}，数量:{}", userId, groupId, messageInfos.size());
        return messageInfos;
    }

    private List<Long> getReadedUserIds(Map<Object, Object> maxIdMap, Long messageId, Long sendId) {
        List<Long> userIds = new LinkedList<>();
        maxIdMap.forEach((k, v) -> {
            Long userId = Long.valueOf(k.toString());
            long maxId = Long.parseLong(v.toString());
            // 发送者不计入已读人数
            if (!sendId.equals(userId) && maxId >= messageId) {
                userIds.add(userId);
            }
        });
        return userIds;
    }

    private void sendLoadingMessage(Boolean isLoadding) {
        GroupMessageVO msgInfo = new GroupMessageVO();
        msgInfo.setType(MessageType.LOADING.code());
        msgInfo.setContent(isLoadding.toString());
        IMGroupMessage sendMessage = new IMGroupMessage<>();
        sendMessage.setSender(new IMUserInfo(SecurityFrameworkUtils.getLoginImUserId(), SecurityFrameworkUtils.getLoginTerminate()));
        sendMessage.setRecvIds(Arrays.asList(SecurityFrameworkUtils.getLoginImUserId()));
        sendMessage.setRecvTerminals(Arrays.asList(SecurityFrameworkUtils.getLoginTerminate()));
        sendMessage.setData(msgInfo);
        sendMessage.setSendToSelf(false);
        sendMessage.setSendResult(false);
        imClient.sendGroupMessage(sendMessage);
    }

}
