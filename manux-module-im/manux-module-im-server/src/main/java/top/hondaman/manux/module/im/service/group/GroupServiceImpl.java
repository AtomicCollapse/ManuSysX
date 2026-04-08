package top.hondaman.manux.module.im.service.group;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.lock.annotation.Lock4j;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.hondaman.manux.framework.security.core.util.SecurityFrameworkUtils;
import top.hondaman.manux.module.im.api.group.dto.GroupDndDTO;
import top.hondaman.manux.module.im.api.group.dto.GroupInviteDTO;
import top.hondaman.manux.module.im.api.group.dto.GroupMemberRemoveDTO;
import top.hondaman.manux.module.im.controller.admin.group.vo.GroupMemberVO;
import top.hondaman.manux.module.im.controller.admin.group.vo.GroupVO;
import top.hondaman.manux.module.im.controller.admin.groupmessage.vo.GroupMessageVO;
import top.hondaman.manux.module.im.dal.dataobject.friend.FriendDO;
import top.hondaman.manux.module.im.dal.dataobject.group.GroupDO;
import top.hondaman.manux.module.im.dal.dataobject.groupmember.GroupMemberDO;
import top.hondaman.manux.module.im.dal.dataobject.groupmessage.GroupMessageDO;
import top.hondaman.manux.module.im.dal.dataobject.user.IMUserDO;
import top.hondaman.manux.module.im.dal.mysql.group.GroupMapper;
import top.hondaman.manux.module.im.dal.mysql.groupmessage.GroupMessageMapper;
import top.hondaman.manux.module.im.enums.IMPlatformConstant;
import top.hondaman.manux.module.im.enums.IMPlatformRedisKey;
import top.hondaman.manux.module.im.enums.MessageStatus;
import top.hondaman.manux.module.im.enums.MessageType;
import top.hondaman.manux.module.im.framework.im.core.IMClient;
import top.hondaman.manux.module.im.framework.im.model.IMGroupMessage;
import top.hondaman.manux.module.im.framework.im.model.IMUserInfo;
import top.hondaman.manux.module.im.service.friend.FriendService;
import top.hondaman.manux.module.im.service.groupmember.GroupMemberService;
import top.hondaman.manux.module.im.service.user.UserService;
import top.hondaman.manux.module.im.util.CommaTextUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static top.hondaman.manux.framework.common.exception.util.ServiceExceptionUtil.exception;
import static top.hondaman.manux.module.im.enums.ErrorCodeConstants.*;

@Slf4j
@Service
@CacheConfig(cacheNames = IMPlatformRedisKey.IM_CACHE_GROUP)
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {
    @Resource
    private UserService userService;
    @Resource
    private GroupMemberService groupMemberService;
    @Resource
    private GroupMessageMapper groupMessageMapper;
    @Resource
    private FriendService friendsService;
    @Resource
    private IMClient imClient;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public GroupVO createGroup(GroupVO vo) {
        IMUserDO user = userService.getById(SecurityFrameworkUtils.getLoginImUserId());
        // 保存群组数据
        GroupDO group = BeanUtil.copyProperties(vo, GroupDO.class);
        group.setOwnerId(user.getId());
        this.save(group);
        // 把群主加入群
        GroupMemberDO member = new GroupMemberDO();
        member.setGroupId(group.getId());
        member.setUserId(user.getId());
        member.setHeadImage(user.getHeadImageThumb());
        member.setUserNickName(user.getNickName());
        member.setRemarkNickName(vo.getRemarkNickName());
        member.setRemarkGroupName(vo.getRemarkGroupName());
        groupMemberService.save(member);
        GroupVO groupVo = findById(group.getId());
        // 推送同步消息给自己的其他终端
        sendAddGroupMessage(groupVo, new ArrayList<>(), true);
        // 返回
        log.info("创建群聊，群聊id:{},群聊名称:{}", group.getId(), group.getName());
        return groupVo;
    }

    @CacheEvict(key = "#vo.getId()")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public GroupVO modifyGroup(GroupVO vo) {
        // 校验是不是群主，只有群主能改信息
        GroupDO group = this.getAndCheckById(vo.getId());
        // 更新成员信息
        GroupMemberDO member = groupMemberService.findByGroupAndUserId(vo.getId(), SecurityFrameworkUtils.getLoginImUserId());
        if (Objects.isNull(member) || member.getQuit()) {
            throw exception(NOT_MEMBER_OF_GROUP);
        }
        member.setRemarkNickName(vo.getRemarkNickName());
        member.setRemarkGroupName(vo.getRemarkGroupName());
        groupMemberService.updateById(member);
        // 群主有权修改群基本信息
        if (group.getOwnerId().equals(SecurityFrameworkUtils.getLoginImUserId())) {
            group = BeanUtil.copyProperties(vo, GroupDO.class);
            this.updateById(group);
        }
        log.info("修改群聊，群聊id:{},群聊名称:{}", group.getId(), group.getName());
        return convert(group, member);
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(key = "#groupId")
    @Override
    public void deleteGroup(Long groupId) {
        GroupDO group = this.getById(groupId);
        if (!group.getOwnerId().equals(SecurityFrameworkUtils.getLoginImUserId())) {
            throw exception(NOT_GROUP_OWNER_DELETE);
        }
        // 群聊用户id
        List<Long> userIds = groupMemberService.findUserIdsByGroupId(groupId);
        // 逻辑删除群数据
        group.setDissolve(true);
        this.updateById(group);
        // 删除成员数据
        groupMemberService.removeByGroupId(groupId);
        // 清理已读缓存
        String key = StrUtil.join(":", IMPlatformRedisKey.IM_GROUP_READED_POSITION, groupId);
        redisTemplate.delete(key);
        // 推送解散群聊提示
        IMUserDO user = userService.getById(SecurityFrameworkUtils.getLoginImUserId());
        String content = String.format("'%s'解散了群聊", user.getNickName());
        this.sendTipMessage(groupId, userIds, content, true);
        // 推送同步消息
        this.sendDelGroupMessage(groupId, userIds);
        log.info("删除群聊，群聊id:{},群聊名称:{}", group.getId(), group.getName());
    }

    @Override
    public void quitGroup(Long groupId) {
        Long userId = SecurityFrameworkUtils.getLoginImUserId();
        GroupDO group = this.getById(groupId);
        if (group.getOwnerId().equals(userId)) {
            throw exception(GROUP_OWNER_QUITE);
        }
        // 删除群聊成员
        groupMemberService.removeByGroupAndUserId(groupId, userId);
        // 清理已读缓存
        String key = StrUtil.join(":", IMPlatformRedisKey.IM_GROUP_READED_POSITION, groupId);
        redisTemplate.opsForHash().delete(key, userId.toString());
        // 推送退出群聊提示
        this.sendTipMessage(groupId, List.of(userId), "您已退出群聊", false);
        // 推送同步消息
        this.sendDelGroupMessage(groupId, List.of(userId));
        log.info("退出群聊，群聊id:{},群聊名称:{},用户id:{}", group.getId(), group.getName(), userId);
    }

    @Override
    public void kickGroup(Long groupId, Long userId) {
        GroupDO group = this.getAndCheckById(groupId);
        if (!group.getOwnerId().equals(SecurityFrameworkUtils.getLoginImUserId())) {
            throw exception(GROUP_NOT_OWNER_KICK);
        }
        if (userId.equals(SecurityFrameworkUtils.getLoginImUserId())) {
            throw exception(GROUP_KICK_SELF);
        }
        // 删除群聊成员
        groupMemberService.removeByGroupAndUserId(groupId, userId);
        // 清理已读缓存
        String key = StrUtil.join(":", IMPlatformRedisKey.IM_GROUP_READED_POSITION, groupId);
        redisTemplate.opsForHash().delete(key, userId.toString());
        // 推送踢出群聊提示
        this.sendTipMessage(groupId, List.of(userId), "您已被移出群聊", false);
        // 推送同步消息
        this.sendDelGroupMessage(groupId, List.of(userId));
        log.info("踢出群聊，群聊id:{},群聊名称:{},用户id:{}", group.getId(), group.getName(), userId);
    }

    @Override
    public void removeGroupMembers(GroupMemberRemoveDTO dto) {
        GroupDO group = this.getAndCheckById(dto.getGroupId());
        if (!group.getOwnerId().equals(SecurityFrameworkUtils.getLoginImUserId())) {
            throw exception(NO_PERMISSION);
        }
        if (dto.getUserIds().contains(group.getOwnerId())) {
            throw exception(GROUP_REMOVE_OWNER);
        }
        if (dto.getUserIds().contains(SecurityFrameworkUtils.getLoginImUserId())) {
            throw exception(GROUP_KICK_SELF);
        }
        // 删除群聊成员
        groupMemberService.removeByGroupAndUserIds(dto.getGroupId(), dto.getUserIds());
        // 清理已读缓存
        String key = StrUtil.join(":", IMPlatformRedisKey.IM_GROUP_READED_POSITION, dto.getGroupId());
        dto.getUserIds().forEach(id -> redisTemplate.opsForHash().delete(key, id.toString()));
        // 推送踢出群聊提示
        this.sendTipMessage(dto.getGroupId(), dto.getUserIds(), "您已被移出群聊", false);
        // 推送同步消息
        this.sendDelGroupMessage(dto.getGroupId(), dto.getUserIds());
        log.info("踢出群聊，群聊id:{},群聊名称:{},用户id:{}", group.getId(), group.getName(), dto.getUserIds());
    }

    @Override
    public GroupVO findById(Long groupId) {
        GroupDO group = super.getById(groupId);
        if (Objects.isNull(group)) {
            throw exception(GROUP_NOT_EXISTS);
        }
        GroupMemberDO member = groupMemberService.findByGroupAndUserId(groupId, SecurityFrameworkUtils.getLoginImUserId());
        if (Objects.isNull(member)) {
            throw exception(NOT_JOINED_GROUP);
        }
        return convert(group, member);
    }

    @Cacheable(key = "#groupId")
    @Override
    public GroupDO getAndCheckById(Long groupId) {
        GroupDO group = super.getById(groupId);
        if (Objects.isNull(group)) {
            throw exception(GROUP_NOT_EXISTS);
        }
        if (group.getDissolve()) {
            throw exception(GROUP_DISSOLVED,group.getName());
        }
        if (group.getIsBanned()) {
            throw exception(GROUP_IS_BANNED,group.getName(),group.getReason());
        }
        return group;
    }

    @Override
    public List<GroupVO> findGroups() {
        // 查询当前用户的群id列表
        List<GroupMemberDO> groupMembers = groupMemberService.findByUserId(SecurityFrameworkUtils.getLoginImUserId());
        // 一个月内退的群可能存在退群前的离线消息,一并返回作为前端缓存
        groupMembers.addAll(groupMemberService.findQuitInMonth(SecurityFrameworkUtils.getLoginImUserId()));
        if (groupMembers.isEmpty()) {
            return new LinkedList<>();
        }
        // 拉取群列表
        List<Long> ids = groupMembers.stream().map((GroupMemberDO::getGroupId)).collect(Collectors.toList());
        LambdaQueryWrapper<GroupDO> groupWrapper = Wrappers.lambdaQuery();
        groupWrapper.in(GroupDO::getId, ids);
        List<GroupDO> groups = this.list(groupWrapper);
        // 转vo
        return groups.stream().map(group -> {
            GroupMemberDO member =
                groupMembers.stream().filter(m -> group.getId().equals(m.getGroupId())).findFirst().get();
            return convert(group, member);
        }).collect(Collectors.toList());
    }

    @Lock4j(name = IMPlatformRedisKey.IM_LOCK_GROUP_ENTER,keys = "#dto.getGroupId()")
    @Override
    public void invite(GroupInviteDTO dto) {
        GroupDO group = this.getAndCheckById(dto.getGroupId());
        GroupMemberDO member = groupMemberService.findByGroupAndUserId(dto.getGroupId(), SecurityFrameworkUtils.getLoginImUserId());
        if (Objects.isNull(group) || member.getQuit()) {
            throw exception(NOT_IN_GROUP_INVITE);
        }
        // 群聊人数校验
        List<GroupMemberDO> members = groupMemberService.findByGroupId(dto.getGroupId());
        long size = members.stream().filter(m -> !m.getQuit()).count();
        if (dto.getFriendIds().size() + size > IMPlatformConstant.MAX_LARGE_GROUP_MEMBER) {
            throw exception(GROUP_MAX_OVER,IMPlatformConstant.MAX_LARGE_GROUP_MEMBER);
        }
        // 找出好友信息
        List<FriendDO> friends = friendsService.findByFriendIds(dto.getFriendIds());
        if (dto.getFriendIds().size() != friends.size()) {
            throw exception(PART_NOT_IN_GROUP_INVITE);
        }
        // 批量保存成员数据
        List<GroupMemberDO> groupMembers = friends.stream().map(f -> {
            Optional<GroupMemberDO> optional =
                members.stream().filter(m -> m.getUserId().equals(f.getFriendId())).findFirst();
            GroupMemberDO groupMember = optional.orElseGet(GroupMemberDO::new);
            groupMember.setGroupId(dto.getGroupId());
            groupMember.setUserId(f.getFriendId());
            groupMember.setUserNickName(f.getFriendNickName());
            groupMember.setHeadImage(f.getFriendHeadImage());
            groupMember.setQuit(false);
            return groupMember;
        }).collect(Collectors.toList());
        if (!groupMembers.isEmpty()) {
            groupMemberService.saveOrUpdateBatch(group.getId(), groupMembers);
        }
        // 推送同步消息给被邀请人
        for (GroupMemberDO m : groupMembers) {
            GroupVO groupVo = convert(group, m);
            sendAddGroupMessage(groupVo, List.of(m.getUserId()), false);
        }
        // 推送进入群聊消息
        List<Long> userIds = groupMemberService.findUserIdsByGroupId(dto.getGroupId());
        String memberNames = groupMembers.stream().map(GroupMemberDO::getShowNickName).collect(Collectors.joining(","));
        IMUserDO user = userService.getById(SecurityFrameworkUtils.getLoginImUserId());
        String content = String.format("'%s'邀请'%s'加入了群聊", user.getNickName(), memberNames);
        this.sendTipMessage(dto.getGroupId(), userIds, content, true);
        log.info("邀请进入群聊，群聊id:{},群聊名称:{},被邀请用户id:{}", group.getId(), group.getName(),
            dto.getFriendIds());
    }

    @Override
    public List<GroupMemberVO> findGroupMembers(Long groupId) {
        GroupDO group = getAndCheckById(groupId);
        List<GroupMemberDO> members = groupMemberService.findByGroupId(groupId);
        List<Long> userIds = members.stream().map(GroupMemberDO::getUserId).collect(Collectors.toList());
        List<Long> onlineUserIds = imClient.getOnlineUser(userIds);
        return members.stream().map(m -> {
            GroupMemberVO vo = BeanUtil.copyProperties(m, GroupMemberVO.class);
            vo.setShowNickName(m.getShowNickName());
            vo.setShowGroupName(StrUtil.blankToDefault(m.getRemarkGroupName(), group.getName()));
            vo.setOnline(onlineUserIds.contains(m.getUserId()));
            return vo;
        }).sorted((m1, m2) -> m2.getOnline().compareTo(m1.getOnline())).collect(Collectors.toList());
    }

    @Override
    public void setDnd(GroupDndDTO dto) {
        groupMemberService.setDnd(dto.getGroupId(), SecurityFrameworkUtils.getLoginImUserId(), dto.getIsDnd());
        // 推送同步消息
        sendSyncDndMessage(dto.getGroupId(), dto.getIsDnd());
    }


    private void sendTipMessage(Long groupId, List<Long> recvIds, String content, Boolean sendToAll) {
        IMUserDO user = userService.getById(SecurityFrameworkUtils.getLoginImUserId());
        // 消息入库
        GroupMessageDO message = new GroupMessageDO();
        message.setContent(content);
        message.setType(MessageType.TIP_TEXT.code());
        message.setStatus(MessageStatus.PENDING.code());
        message.setSendTime(LocalDateTime.now());
        message.setSendNickName(user.getNickName());
        message.setGroupId(groupId);
        message.setSendId(SecurityFrameworkUtils.getLoginImUserId());
        message.setRecvIds(sendToAll ? "" : CommaTextUtils.asText(recvIds));
        groupMessageMapper.insert(message);
        // 推送
        GroupMessageVO msgInfo = BeanUtil.copyProperties(message, GroupMessageVO.class);
        IMGroupMessage<GroupMessageVO> sendMessage = new IMGroupMessage<>();
        sendMessage.setSender(new IMUserInfo(SecurityFrameworkUtils.getLoginImUserId(), SecurityFrameworkUtils.getLoginTerminate()));
        if (CollUtil.isEmpty(recvIds)) {
            // 为空表示向全体发送
            List<Long> userIds = groupMemberService.findUserIdsByGroupId(groupId);
            sendMessage.setRecvIds(userIds);
        } else {
            sendMessage.setRecvIds(recvIds);
        }
        sendMessage.setData(msgInfo);
        sendMessage.setSendResult(false);
        sendMessage.setSendToSelf(false);
        imClient.sendGroupMessage(sendMessage);
    }

    private GroupVO convert(GroupDO group, GroupMemberDO member) {
        GroupVO vo = BeanUtil.copyProperties(group, GroupVO.class);
        vo.setRemarkGroupName(member.getRemarkGroupName());
        vo.setRemarkNickName(member.getRemarkNickName());
        vo.setShowNickName(member.getShowNickName());
        vo.setShowGroupName(StrUtil.blankToDefault(member.getRemarkGroupName(), group.getName()));
        vo.setQuit(member.getQuit());
        vo.setIsDnd(member.getIsDnd());
        return vo;
    }

    private void sendAddGroupMessage(GroupVO group, List<Long> recvIds, Boolean sendToSelf) {
        GroupMessageVO msgInfo = new GroupMessageVO();
        msgInfo.setContent(JSON.toJSONString(group));
        msgInfo.setType(MessageType.GROUP_NEW.code());
        msgInfo.setSendTime(LocalDateTime.now());
        msgInfo.setGroupId(group.getId());
        msgInfo.setSendId(SecurityFrameworkUtils.getLoginImUserId());
        IMGroupMessage<GroupMessageVO> sendMessage = new IMGroupMessage<>();
        sendMessage.setSender(new IMUserInfo(SecurityFrameworkUtils.getLoginImUserId(), SecurityFrameworkUtils.getLoginTerminate()));
        sendMessage.setRecvIds(recvIds);
        sendMessage.setData(msgInfo);
        sendMessage.setSendResult(false);
        sendMessage.setSendToSelf(sendToSelf);
        imClient.sendGroupMessage(sendMessage);
    }

    private void sendDelGroupMessage(Long groupId, List<Long> recvIds) {
        GroupMessageVO msgInfo = new GroupMessageVO();
        msgInfo.setType(MessageType.GROUP_DEL.code());
        msgInfo.setSendTime(LocalDateTime.now());
        msgInfo.setGroupId(groupId);
        msgInfo.setSendId(SecurityFrameworkUtils.getLoginImUserId());
        IMGroupMessage<GroupMessageVO> sendMessage = new IMGroupMessage<>();
        sendMessage.setSender(new IMUserInfo(SecurityFrameworkUtils.getLoginImUserId(), SecurityFrameworkUtils.getLoginTerminate()));
        sendMessage.setRecvIds(recvIds);
        sendMessage.setData(msgInfo);
        sendMessage.setSendResult(false);
        sendMessage.setSendToSelf(false);
        imClient.sendGroupMessage(sendMessage);
    }

    private void sendSyncDndMessage(Long groupId, Boolean isDnd) {
        GroupMessageVO msgInfo = new GroupMessageVO();
        msgInfo.setType(MessageType.GROUP_DND.code());
        msgInfo.setSendTime(LocalDateTime.now());
        msgInfo.setGroupId(groupId);
        msgInfo.setSendId(SecurityFrameworkUtils.getLoginImUserId());
        msgInfo.setContent(isDnd.toString());
        IMGroupMessage<GroupMessageVO> sendMessage = new IMGroupMessage<>();
        sendMessage.setSender(new IMUserInfo(SecurityFrameworkUtils.getLoginImUserId(), SecurityFrameworkUtils.getLoginTerminate()));
        sendMessage.setData(msgInfo);
        sendMessage.setSendResult(false);
        imClient.sendGroupMessage(sendMessage);
    }

}
