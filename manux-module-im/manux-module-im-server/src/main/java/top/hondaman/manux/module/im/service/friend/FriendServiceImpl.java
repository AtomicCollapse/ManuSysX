package top.hondaman.manux.module.im.service.friend;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.lock.annotation.Lock4j;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.hondaman.manux.framework.mybatis.core.query.LambdaQueryWrapperX;
import top.hondaman.manux.framework.security.core.util.SecurityFrameworkUtils;
import top.hondaman.manux.module.im.api.friend.dto.FriendDndDTO;
import top.hondaman.manux.module.im.controller.admin.friend.vo.FriendVO;
import top.hondaman.manux.module.im.controller.admin.privatemessage.vo.PrivateMessageVO;
import top.hondaman.manux.module.im.dal.dataobject.friend.FriendDO;
import top.hondaman.manux.module.im.dal.dataobject.privatemessage.PrivateMessageDO;
import top.hondaman.manux.module.im.dal.dataobject.user.IMUserDO;
import top.hondaman.manux.module.im.dal.mysql.friend.FriendMapper;
import top.hondaman.manux.module.im.dal.mysql.privatemessage.PrivateMessageMapper;
import top.hondaman.manux.module.im.dal.mysql.user.IMUserMapper;
import top.hondaman.manux.module.im.enums.IMPlatformRedisKey;
import top.hondaman.manux.module.im.enums.IMTerminalType;
import top.hondaman.manux.module.im.enums.MessageStatus;
import top.hondaman.manux.module.im.enums.MessageType;
import top.hondaman.manux.module.im.framework.im.core.IMClient;
import top.hondaman.manux.module.im.framework.im.model.IMPrivateMessage;
import top.hondaman.manux.module.im.framework.im.model.IMUserInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static top.hondaman.manux.framework.common.exception.util.ServiceExceptionUtil.exception;
import static top.hondaman.manux.module.im.enums.ErrorCodeConstants.ADD_SELF_FRIEND;
import static top.hondaman.manux.module.im.enums.ErrorCodeConstants.NOT_YOUR_FRIEND;

@Slf4j
@Service
@CacheConfig(cacheNames = IMPlatformRedisKey.IM_CACHE_FRIEND)
public class FriendServiceImpl extends ServiceImpl<FriendMapper, FriendDO> implements FriendService {
    @Resource
    @Lazy // 使用延迟加载避免循环依赖
    private FriendService self;
    @Resource
    private PrivateMessageMapper privateMessageMapper;
    @Resource
    private FriendMapper friendMapper;
    @Resource
    private IMClient imClient;
    @Resource
    private IMUserMapper userMapper;

    @Override
    public List<FriendDO> findAllFriends() {
        Long userId = SecurityFrameworkUtils.getLoginImUserId();
        LambdaQueryWrapper<FriendDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(FriendDO::getUserId, userId);
        return friendMapper.selectList(wrapper);
    }

    @Override
    public List<FriendDO> findByFriendIds(List<Long> friendIds) {
        Long userId = SecurityFrameworkUtils.getLoginImUserId();
        return friendMapper.selectList(new LambdaQueryWrapperX<FriendDO>()
                .eq(FriendDO::getUserId, userId)
                .in(FriendDO::getFriendId, friendIds)
        );
    }

    @Override
    public List<FriendVO> findFriends() {
        List<FriendDO> friends = this.findAllFriends();
        return friends.stream().map(this::conver).collect(Collectors.toList());
    }

    @Lock4j(name = IMPlatformRedisKey.IM_LOCK_FRIEND_ADD, keys = "#userId+':'+#friendId")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addFriend(Long userId, Long friendId) {
        if (friendId.equals(userId)) {
            throw exception(ADD_SELF_FRIEND);
        }
        // 互相绑定好友关系
        self.bindFriend(userId, friendId);
        self.bindFriend(friendId, userId);
        // 推送添加好友提示
        sendAddTipMessage(friendId);
        log.info("添加好友，用户id:{},好友id:{}", userId, friendId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delFriend(Long friendId) {
        Long userId = SecurityFrameworkUtils.getLoginImUserId();
        // 互相解除好友关系，走代理清理缓存
        self.unbindFriend(userId, friendId);
        self.unbindFriend(friendId, userId);
        // 推送解除好友提示
        sendDelTipMessage(friendId);
        log.info("删除好友，用户id:{},好友id:{}", userId, friendId);
    }

    @Cacheable(key = "#userId1+':'+#userId2")
    @Override
    public Boolean isFriend(Long userId1, Long userId2) {
        return friendMapper.exists(new LambdaQueryWrapperX<FriendDO>()
                .eq(FriendDO::getUserId, userId1)
                .eq(FriendDO::getFriendId, userId2)
        );
    }

    /**
     * 单向绑定好友关系
     *
     * @param userId   用户id
     * @param friendId 好友的用户id
     */
    @CacheEvict(key = "#userId+':'+#friendId")
    public void bindFriend(Long userId, Long friendId) {
        QueryWrapper<FriendDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(FriendDO::getUserId, userId).eq(FriendDO::getFriendId, friendId);
        FriendDO friend = friendMapper.selectOne(wrapper);
        if (Objects.isNull(friend)) {
            friend = new FriendDO();
        } else {
            return;
        }
        friend.setUserId(userId);
        friend.setFriendId(friendId);
        IMUserDO friendInfo = userMapper.selectById(friendId);
        friend.setFriendHeadImage(friendInfo.getHeadImageThumb());
        friend.setFriendNickName(friendInfo.getNickName());
        friend.setDeleted(false);
        friendMapper.insert(friend);
        // 推送好友变化信息
        sendAddFriendMessage(userId, friendId, friend);
    }

    @Override
    public void setDnd(FriendDndDTO dto) {
        LambdaUpdateWrapper<FriendDO> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(FriendDO::getUserId, SecurityFrameworkUtils.getLoginImUserId());
        wrapper.eq(FriendDO::getFriendId, dto.getFriendId());
        wrapper.set(FriendDO::getIsDnd, dto.getIsDnd());
        friendMapper.update(wrapper);
        // 推送同步消息
        sendSyncDndMessage(dto.getFriendId(), dto.getIsDnd());
    }

    /**
     * 单向解除好友关系
     *
     * @param userId   用户id
     * @param friendId 好友的用户id
     */
    @CacheEvict(key = "#userId+':'+#friendId")
    public void unbindFriend(Long userId, Long friendId) {
        // 逻辑删除
        friendMapper.delete(new LambdaQueryWrapperX<FriendDO>()
                .eq(FriendDO::getUserId, userId)
                .eq(FriendDO::getFriendId, friendId)
        );
        // 推送好友变化信息
        sendDelFriendMessage(userId, friendId);
    }

    @Override
    public FriendVO findFriend(Long friendId) {
        LambdaQueryWrapper<FriendDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(FriendDO::getUserId, SecurityFrameworkUtils.getLoginImUserId());
        wrapper.eq(FriendDO::getFriendId, friendId);
        FriendDO friend = friendMapper.selectOne(wrapper);
        if (Objects.isNull(friend)) {
            throw exception(NOT_YOUR_FRIEND);
        }
        return conver(friend);
    }

    private FriendVO conver(FriendDO f) {
        FriendVO vo = new FriendVO();
        vo.setId(f.getFriendId());
        vo.setHeadImage(f.getFriendHeadImage());
        vo.setNickName(f.getFriendNickName());
        vo.setDeleted(f.getDeleted());
        vo.setIsDnd(f.getIsDnd());
        return vo;
    }

    void sendAddFriendMessage(Long userId, Long friendId, FriendDO friend) {
        // 推送好友状态信息
        PrivateMessageVO msgInfo = new PrivateMessageVO();
        msgInfo.setSendId(friendId);
        msgInfo.setRecvId(userId);
        msgInfo.setSendTime(LocalDateTime.now());
        msgInfo.setType(MessageType.FRIEND_NEW.code());
        FriendVO vo = conver(friend);
        msgInfo.setContent(JSON.toJSONString(vo));
        IMPrivateMessage<PrivateMessageVO> sendMessage = new IMPrivateMessage<>();
        sendMessage.setSender(new IMUserInfo(friendId, IMTerminalType.UNKNOW.code()));
        sendMessage.setRecvId(userId);
        sendMessage.setData(msgInfo);
        sendMessage.setSendToSelf(false);
        sendMessage.setSendResult(false);
        imClient.sendPrivateMessage(sendMessage);
    }

    void sendDelFriendMessage(Long userId, Long friendId) {
        // 推送好友状态信息
        PrivateMessageVO msgInfo = new PrivateMessageVO();
        msgInfo.setSendId(friendId);
        msgInfo.setRecvId(userId);
        msgInfo.setSendTime(LocalDateTime.now());
        msgInfo.setType(MessageType.FRIEND_DEL.code());
        IMPrivateMessage<PrivateMessageVO> sendMessage = new IMPrivateMessage<>();
        sendMessage.setSender(new IMUserInfo(friendId, IMTerminalType.UNKNOW.code()));
        sendMessage.setRecvId(userId);
        sendMessage.setData(msgInfo);
        sendMessage.setSendToSelf(false);
        sendMessage.setSendResult(false);
        imClient.sendPrivateMessage(sendMessage);
    }

    void sendAddTipMessage(Long friendId) {
        PrivateMessageDO msg = new PrivateMessageDO();
        msg.setSendId(SecurityFrameworkUtils.getLoginImUserId());
        msg.setRecvId(friendId);
        msg.setContent("你们已成为好友，现在可以开始聊天了");
        msg.setSendTime(LocalDateTime.now());
        msg.setStatus(MessageStatus.PENDING.code());
        msg.setType(MessageType.TIP_TEXT.code());
        privateMessageMapper.insert(msg);
        // 推给对方
        PrivateMessageVO messageInfo = BeanUtil.copyProperties(msg, PrivateMessageVO.class);
        IMPrivateMessage<PrivateMessageVO> sendMessage = new IMPrivateMessage<>();
        sendMessage.setSender(new IMUserInfo(SecurityFrameworkUtils.getLoginImUserId(), SecurityFrameworkUtils.getLoginTerminate()));
        sendMessage.setRecvId(friendId);
        sendMessage.setSendToSelf(false);
        sendMessage.setData(messageInfo);
        imClient.sendPrivateMessage(sendMessage);
        // 推给自己
        sendMessage.setRecvId(SecurityFrameworkUtils.getLoginImUserId());
        imClient.sendPrivateMessage(sendMessage);
    }

    void sendDelTipMessage(Long friendId){
        // 推送好友状态信息
        PrivateMessageDO msg = new PrivateMessageDO();
        msg.setSendId(SecurityFrameworkUtils.getLoginImUserId());
        msg.setRecvId(friendId);
        msg.setSendTime(LocalDateTime.now());
        msg.setType(MessageType.TIP_TEXT.code());
        msg.setStatus(MessageStatus.PENDING.code());
        msg.setContent("你们的好友关系已被解除");
        privateMessageMapper.insert(msg);
        // 推送
        PrivateMessageVO messageInfo = BeanUtil.copyProperties(msg, PrivateMessageVO.class);
        IMPrivateMessage<PrivateMessageVO> sendMessage = new IMPrivateMessage<>();
        sendMessage.setSender(new IMUserInfo(friendId, IMTerminalType.UNKNOW.code()));
        sendMessage.setRecvId(friendId);
        sendMessage.setData(messageInfo);
        imClient.sendPrivateMessage(sendMessage);
    }

    void sendSyncDndMessage(Long friendId, Boolean isDnd) {
        // 同步免打扰状态到其他终端
        PrivateMessageVO msgInfo = new PrivateMessageVO();
        msgInfo.setSendId(SecurityFrameworkUtils.getLoginImUserId());
        msgInfo.setRecvId(friendId);
        msgInfo.setSendTime(LocalDateTime.now());
        msgInfo.setType(MessageType.FRIEND_DND.code());
        msgInfo.setContent(isDnd.toString());
        IMPrivateMessage<PrivateMessageVO> sendMessage = new IMPrivateMessage<>();
        sendMessage.setSender(new IMUserInfo(SecurityFrameworkUtils.getLoginImUserId(), SecurityFrameworkUtils.getLoginTerminate()));
        sendMessage.setData(msgInfo);
        sendMessage.setSendToSelf(true);
        imClient.sendPrivateMessage(sendMessage);
    }

}
