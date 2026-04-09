package top.hondaman.manux.module.im.dal.dataobject.user;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import top.hondaman.manux.framework.mybatis.core.dataobject.BaseDO;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * im用户
 */
@TableName("im_user")
@KeySequence("im_user_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IMUserDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String headImage;

    /**
     * 用户头像缩略图
     */
    private String headImageThumb;

    /**
     * 密码(明文)
     */
    private String password;

    /**
     * 性别 0:男 1::女
     */
    private Integer sex;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 账号是否被封禁
     */
    private Boolean isBanned;

    /**
     * 账号被封禁原因
     */
    private String reason;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     *  账号类型 1:普通用户 2:wx小程序审核账户
     */
    private Integer type;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 用户id 根据user_type关联 member_user表 或 system_user表
     */
    private Long userId;
}
