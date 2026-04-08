package top.hondaman.manux.module.im.dal.dataobject.groupmessage;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import top.hondaman.manux.framework.mybatis.core.dataobject.BaseDO;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 群聊消息
 */
@TableName("im_group_message")
@KeySequence("im_group_message_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMessageDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 临时id,由前端生成
     * 作用:如果用户正在发送消息时掉线了，可以通过此字段获取该消息的实际发送状态
     */
    private String tmpId;

    /**
     * 群id
     */
    private Long groupId;

    /**
     * 发送用户id
     */
    private Long sendId;

    /**
     * 发送用户昵称
     */
    private String sendNickName;

    /**
     * 接受用户id,为空表示全体发送
     */
    private String recvIds;

    /**
     * @用户列表
     */
    private String atUserIds;
    /**
     * 发送内容
     */
    private String content;

    /**
     * 消息类型 MessageType
     */
    private Integer type;

    /**
     *  是否回执消息
     */
    private Boolean receipt;

    /**
     *  回执消息是否完成
     */
    private Boolean receiptOk;

    /**
     * 状态 MessageStatus
     */
    private Integer status;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

}
