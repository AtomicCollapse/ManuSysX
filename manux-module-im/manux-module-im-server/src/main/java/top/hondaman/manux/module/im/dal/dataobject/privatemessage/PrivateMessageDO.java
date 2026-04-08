package top.hondaman.manux.module.im.dal.dataobject.privatemessage;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import top.hondaman.manux.framework.mybatis.core.dataobject.BaseDO;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 私聊消息
 */
@TableName("im_private_message")
@KeySequence("im_private_message_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivateMessageDO extends BaseDO {

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
     * 发送用户id
     */
    private Long sendId;

    /**
     * 接收用户id
     */
    private Long recvId;

    /**
     * 发送内容
     */
    private String content;

    /**
     * 消息类型 MessageType
     */
    private Integer type;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

}
