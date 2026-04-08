package top.hondaman.manux.module.im.service.friend;

import com.baomidou.mybatisplus.extension.service.IService;
import top.hondaman.manux.module.im.api.friend.dto.FriendDndDTO;
import top.hondaman.manux.module.im.controller.admin.friend.vo.FriendVO;
import top.hondaman.manux.module.im.dal.dataobject.friend.FriendDO;

import java.util.List;

public interface FriendService extends IService<FriendDO> {

    /**
     * 判断用户2是否用户1的好友
     *
     * @param userId1 用户1的id
     * @param userId2 用户2的id
     * @return true/false
     */
    Boolean isFriend(Long userId1, Long userId2);

    /**
     * 查询用户的所有好友,包括已删除的
     *
     * @return 好友列表
     */
    List<FriendDO> findAllFriends();

    /**
     * 查询用户的所有好友
     *
     * @param friendIds 好友id
     * @return 好友列表
     */
    List<FriendDO> findByFriendIds(List<Long> friendIds);

    /**
     * 查询当前用户的所有好友
     *
     * @return 好友列表
     */
    List<FriendVO> findFriends();

    /**
     * 添加好友，互相建立好友关系
     *
     * @param userId 用户id
     * @param friendId 好友的用户id
     */
    void addFriend(Long userId,Long friendId);

    /**
     * 删除好友，双方都会解除好友关系
     *
     * @param friendId 好友的用户id
     */
    void delFriend(Long friendId);

    /**
     * 查询指定的某个好友信息
     *
     * @param friendId 好友的用户id
     * @return 好友信息
     */
    FriendVO findFriend(Long friendId);

    /**
     * 绑定好友关系
     *
     * @param userId   好友的id
     * @param friendId 好友的用户id
     * @return 好友信息
     */
    void bindFriend(Long userId, Long friendId);

    /**
     * 设置好友免打扰状态
     * @param dto
     */
    void setDnd(FriendDndDTO dto);

    /**
     * 单向解除好友关系
     *
     * @param userId
     * @param friendId
     */
    void unbindFriend(Long userId, Long friendId);
}