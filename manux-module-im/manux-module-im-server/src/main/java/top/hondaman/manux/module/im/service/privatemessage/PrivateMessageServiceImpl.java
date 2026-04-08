package top.hondaman.manux.module.im.service.privatemessage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.hondaman.manux.framework.security.core.util.SecurityFrameworkUtils;
import top.hondaman.manux.module.im.api.privatemessage.dto.PrivateMessageDTO;
import top.hondaman.manux.module.im.controller.admin.privatemessage.vo.PrivateMessageVO;
import top.hondaman.manux.module.im.dal.dataobject.privatemessage.PrivateMessageDO;
import top.hondaman.manux.module.im.dal.mysql.privatemessage.PrivateMessageMapper;
import top.hondaman.manux.module.im.enums.IMConstant;
import top.hondaman.manux.module.im.enums.IMTerminalType;
import top.hondaman.manux.module.im.enums.MessageStatus;
import top.hondaman.manux.module.im.enums.MessageType;
import top.hondaman.manux.module.im.framework.im.core.IMClient;
import top.hondaman.manux.module.im.framework.im.model.IMPrivateMessage;
import top.hondaman.manux.module.im.framework.im.model.IMUserInfo;
import top.hondaman.manux.module.im.service.friend.FriendService;
import top.hondaman.manux.module.im.util.SensitiveFilterUtil;
import top.hondaman.manux.module.im.util.ThreadPoolExecutorFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.stream.Collectors;

import static top.hondaman.manux.framework.common.exception.util.ServiceExceptionUtil.exception;
import static top.hondaman.manux.module.im.enums.ErrorCodeConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrivateMessageServiceImpl extends ServiceImpl<PrivateMessageMapper, PrivateMessageDO> implements PrivateMessageService {

    @Resource
    private FriendService friendService;
    @Resource
    private IMClient imClient;
    @Resource
    private SensitiveFilterUtil sensitiveFilterUtil;
    
    private static final ScheduledThreadPoolExecutor EXECUTOR = ThreadPoolExecutorFactory.getThreadPoolExecutor();

    @Override
    public PrivateMessageVO sendMessage(PrivateMessageDTO dto) {
        Boolean isFriends = friendService.isFriend(SecurityFrameworkUtils.getLoginImUserId(), dto.getRecvId());
        if (Boolean.FALSE.equals(isFriends)) {
            throw exception(NOT_FRIEND);
        }
        // 保存消息
        PrivateMessageDO msg = BeanUtil.copyProperties(dto, PrivateMessageDO.class);
        msg.setSendId(SecurityFrameworkUtils.getLoginImUserId());
        msg.setStatus(MessageStatus.PENDING.code());
        msg.setSendTime(LocalDateTime.now());
        // 过滤内容中的敏感词
        if (MessageType.TEXT.code().equals(dto.getType())) {
            msg.setContent(sensitiveFilterUtil.filter(dto.getContent()));
        }
        this.save(msg);
        // 推送消息
        PrivateMessageVO msgInfo = BeanUtil.copyProperties(msg, PrivateMessageVO.class);
        IMPrivateMessage<PrivateMessageVO> sendMessage = new IMPrivateMessage<>();
        sendMessage.setSender(new IMUserInfo(SecurityFrameworkUtils.getLoginImUserId(), SecurityFrameworkUtils.getLoginTerminate()));
        sendMessage.setRecvId(msgInfo.getRecvId());
        sendMessage.setSendToSelf(true);
        sendMessage.setData(msgInfo);
        sendMessage.setSendResult(true);
        imClient.sendPrivateMessage(sendMessage);
        log.info("发送私聊消息，发送id:{},接收id:{}，内容:{}", SecurityFrameworkUtils.getLoginImUserId(), dto.getRecvId(), dto.getContent());
        return msgInfo;
    }

    @Transactional
    @Override
    public PrivateMessageVO recallMessage(Long id) {
        PrivateMessageDO msg = this.getById(id);
        if (Objects.isNull(msg)) {
            throw exception(PRIVATE_MESSAGE_NOT_EXISTS);
        }
        if (!msg.getSendId().equals(SecurityFrameworkUtils.getLoginImUserId())) {
            throw exception(PRIVATE_MESSAGE_RECALL_NOT_SELF);
        }
        if (Duration.between(LocalDateTime.now(), msg.getSendTime()).getSeconds() > IMConstant.ALLOW_RECALL_SECOND) {
            throw exception(PRIVATE_MESSAGE_RECALL_OVER_TIME);
        }
        // 修改消息状态
        msg.setStatus(MessageStatus.RECALL.code());
        this.updateById(msg);
        // 生成一条撤回消息
        PrivateMessageDO recallMsg = new PrivateMessageDO();
        recallMsg.setSendId(SecurityFrameworkUtils.getLoginImUserId());
        recallMsg.setStatus(MessageStatus.PENDING.code());
        recallMsg.setSendTime(LocalDateTime.now());
        recallMsg.setRecvId(msg.getRecvId());
        recallMsg.setType(MessageType.RECALL.code());
        recallMsg.setContent(id.toString());
        this.save(recallMsg);
        // 推送消息
        PrivateMessageVO msgInfo = BeanUtil.copyProperties(recallMsg, PrivateMessageVO.class);
        IMPrivateMessage<PrivateMessageVO> sendMessage = new IMPrivateMessage<>();
        sendMessage.setSender(new IMUserInfo(SecurityFrameworkUtils.getLoginImUserId(), SecurityFrameworkUtils.getLoginTerminate()));
        sendMessage.setRecvId(msgInfo.getRecvId());
        sendMessage.setData(msgInfo);
        imClient.sendPrivateMessage(sendMessage);
        log.info("撤回私聊消息，发送id:{},接收id:{}，内容:{}", msg.getSendId(), msg.getRecvId(), msg.getContent());
        return msgInfo;
    }

    @Override
    public List<PrivateMessageVO> findHistoryMessage(Long friendId, Long page, Long size) {
        page = page > 0 ? page : 1;
        size = size > 0 ? size : 10;
        Long userId = SecurityFrameworkUtils.getLoginImUserId();
        long stIdx = (page - 1) * size;
        QueryWrapper<PrivateMessageDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().and(
                wrap -> wrap.and(wp -> wp.eq(PrivateMessageDO::getSendId, userId).eq(PrivateMessageDO::getRecvId, friendId))
                    .or(wp -> wp.eq(PrivateMessageDO::getRecvId, userId).eq(PrivateMessageDO::getSendId, friendId)))
            .ne(PrivateMessageDO::getStatus, MessageStatus.RECALL.code()).orderByDesc(PrivateMessageDO::getId)
            .last("limit " + stIdx + "," + size);

        List<PrivateMessageDO> messages = this.list(wrapper);
        List<PrivateMessageVO> messageInfos =
            messages.stream().map(m -> BeanUtil.copyProperties(m, PrivateMessageVO.class))
                .collect(Collectors.toList());
        log.info("拉取聊天记录，用户id:{},好友id:{}，数量:{}", userId, friendId, messageInfos.size());
        return messageInfos;
    }

    @Override
    public void pullOfflineMessage(Long minId) {
        // 获取当前用户的消息
        LambdaQueryWrapper<PrivateMessageDO> wrapper = Wrappers.lambdaQuery();
        // 只能拉取最近1个月的消息
        Date minDate = DateUtils.addMonths(new Date(), -1);
        wrapper.gt(PrivateMessageDO::getId, minId);
        wrapper.ge(PrivateMessageDO::getSendTime, minDate);
        wrapper.and(wp -> wp.eq(PrivateMessageDO::getSendId, SecurityFrameworkUtils.getLoginImUserId()).or()
            .eq(PrivateMessageDO::getRecvId, SecurityFrameworkUtils.getLoginImUserId()));
        wrapper.orderByAsc(PrivateMessageDO::getId);
        List<PrivateMessageDO> messages = this.list(wrapper);
        // 异步推送消息
        EXECUTOR.execute(() -> {
            // 开启加载中标志
            this.sendLoadingMessage(true);
            for (PrivateMessageDO m : messages) {
                // 推送过程如果用户下线了，则不再推送
                if (!imClient.isOnline(SecurityFrameworkUtils.getLoginImUserId(), IMTerminalType.fromCode(SecurityFrameworkUtils.getLoginTerminate()))) {
                    log.info("用户已下线，停止推送离线私聊消息,用户id:{}", SecurityFrameworkUtils.getLoginImUserId());
                    return;
                }
                PrivateMessageVO vo = BeanUtil.copyProperties(m, PrivateMessageVO.class);
                IMPrivateMessage<PrivateMessageVO> sendMessage = new IMPrivateMessage<>();
                sendMessage.setSender(new IMUserInfo(m.getSendId(), IMTerminalType.WEB.code()));
                sendMessage.setRecvId(SecurityFrameworkUtils.getLoginImUserId());
                sendMessage.setRecvTerminals(List.of(SecurityFrameworkUtils.getLoginTerminate()));
                sendMessage.setSendToSelf(false);
                sendMessage.setData(vo);
                sendMessage.setSendResult(true);
                imClient.sendPrivateMessage(sendMessage);
            }
            // 关闭加载中标志
            this.sendLoadingMessage(false);
            log.info("拉取私聊消息，用户id:{},数量:{}", SecurityFrameworkUtils.getLoginImUserId(), messages.size());
        });
    }

    @Override
    public List<PrivateMessageVO> loadOfflineMessage(Long minId) {
        // 获取当前用户的消息
        LambdaQueryWrapper<PrivateMessageDO> wrapper = Wrappers.lambdaQuery();
        // 只能拉取最近1个月的消息
        Date minDate = DateUtils.addMonths(new Date(), -1);
        wrapper.gt(PrivateMessageDO::getId, minId);
        wrapper.ge(PrivateMessageDO::getSendTime, minDate);
        wrapper.and(wp -> wp.eq(PrivateMessageDO::getSendId, SecurityFrameworkUtils.getLoginImUserId()).or()
            .eq(PrivateMessageDO::getRecvId, SecurityFrameworkUtils.getLoginImUserId()));
        wrapper.orderByAsc(PrivateMessageDO::getId);
        List<PrivateMessageDO> messages = this.list(wrapper);
        // 更新消息为送达状态
        List<Long> messageIds = messages.stream().filter(m -> m.getRecvId().equals(SecurityFrameworkUtils.getLoginImUserId()))
            .filter(m -> m.getStatus().equals(MessageStatus.PENDING.code())).map(PrivateMessageDO::getId)
            .collect(Collectors.toList());
        if (!messageIds.isEmpty()) {
            LambdaUpdateWrapper<PrivateMessageDO> updateWrapper = Wrappers.lambdaUpdate();
            updateWrapper.in(PrivateMessageDO::getId, messageIds);
            updateWrapper.set(PrivateMessageDO::getStatus, MessageStatus.DELIVERED.code());
            update(updateWrapper);
        }
        // 转换vo
        List<PrivateMessageVO> vos = messages.stream().map(m -> BeanUtil.copyProperties(m, PrivateMessageVO.class))
            .collect(Collectors.toList());
        log.info("拉取私聊消息，用户id:{},数量:{},minId:{}", SecurityFrameworkUtils.getLoginImUserId(), messages.size(), minId);
        return vos;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void readedMessage(Long friendId) {
        // 推送消息给自己，清空会话列表上的已读数量
        PrivateMessageVO msgInfo = new PrivateMessageVO();
        msgInfo.setType(MessageType.READED.code());
        msgInfo.setSendId(SecurityFrameworkUtils.getLoginImUserId());
        msgInfo.setRecvId(friendId);
        IMPrivateMessage<PrivateMessageVO> sendMessage = new IMPrivateMessage<>();
        sendMessage.setData(msgInfo);
        sendMessage.setSender(new IMUserInfo(SecurityFrameworkUtils.getLoginImUserId(), SecurityFrameworkUtils.getLoginTerminate()));
        sendMessage.setSendToSelf(true);
        sendMessage.setSendResult(false);
        imClient.sendPrivateMessage(sendMessage);
        // 推送回执消息给对方，更新已读状态
        msgInfo = new PrivateMessageVO();
        msgInfo.setType(MessageType.RECEIPT.code());
        msgInfo.setSendId(SecurityFrameworkUtils.getLoginImUserId());
        msgInfo.setRecvId(friendId);
        sendMessage = new IMPrivateMessage<>();
        sendMessage.setSender(new IMUserInfo(SecurityFrameworkUtils.getLoginImUserId(), SecurityFrameworkUtils.getLoginTerminate()));
        sendMessage.setRecvId(friendId);
        sendMessage.setSendToSelf(false);
        sendMessage.setSendResult(false);
        sendMessage.setData(msgInfo);
        imClient.sendPrivateMessage(sendMessage);
        // 修改消息状态为已读
        LambdaUpdateWrapper<PrivateMessageDO> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(PrivateMessageDO::getSendId, friendId).eq(PrivateMessageDO::getRecvId, SecurityFrameworkUtils.getLoginImUserId())
            .eq(PrivateMessageDO::getStatus, MessageStatus.DELIVERED.code())
            .set(PrivateMessageDO::getStatus, MessageStatus.READED.code());
        this.update(updateWrapper);
        log.info("消息已读，接收方id:{},发送方id:{}", SecurityFrameworkUtils.getLoginImUserId(), friendId);
    }

    @Override
    public Long getMaxReadedId(Long friendId) {
        LambdaQueryWrapper<PrivateMessageDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(PrivateMessageDO::getSendId, SecurityFrameworkUtils.getLoginImUserId()).eq(PrivateMessageDO::getRecvId, friendId)
            .eq(PrivateMessageDO::getStatus, MessageStatus.READED.code()).orderByDesc(PrivateMessageDO::getId)
            .select(PrivateMessageDO::getId).last("limit 1");
        PrivateMessageDO message = this.getOne(wrapper);
        if (Objects.isNull(message)) {
            return -1L;
        }
        return message.getId();
    }

    private void sendLoadingMessage(Boolean isLoadding) {
        PrivateMessageVO msgInfo = new PrivateMessageVO();
        msgInfo.setType(MessageType.LOADING.code());
        msgInfo.setContent(isLoadding.toString());
        IMPrivateMessage<PrivateMessageVO> sendMessage = new IMPrivateMessage<>();
        sendMessage.setSender(new IMUserInfo(SecurityFrameworkUtils.getLoginImUserId(), SecurityFrameworkUtils.getLoginTerminate()));
        sendMessage.setRecvId(SecurityFrameworkUtils.getLoginImUserId());
        sendMessage.setRecvTerminals(List.of(SecurityFrameworkUtils.getLoginTerminate()));
        sendMessage.setData(msgInfo);
        sendMessage.setSendToSelf(false);
        sendMessage.setSendResult(false);
        imClient.sendPrivateMessage(sendMessage);
    }
}
