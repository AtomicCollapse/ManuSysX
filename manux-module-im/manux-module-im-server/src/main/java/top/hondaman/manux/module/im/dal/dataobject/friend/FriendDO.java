package top.hondaman.manux.module.im.dal.dataobject.friend;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import top.hondaman.manux.framework.mybatis.core.dataobject.BaseDO;

import java.util.Date;

/**
 * 好友
 */
@TableName("im_friend")
@KeySequence("im_friend_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendDO extends BaseDO {


    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 好友id
     */
    private Long friendId;

    /**
     * 用户昵称
     */
    private String friendNickName;

    /**
     * 用户头像
     */
    private String friendHeadImage;

    /**
     * 是否开启免打扰
     */
    private Boolean isDnd;
}
