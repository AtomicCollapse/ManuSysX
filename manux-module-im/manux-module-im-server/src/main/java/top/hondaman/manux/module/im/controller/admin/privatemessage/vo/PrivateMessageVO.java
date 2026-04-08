package top.hondaman.manux.module.im.controller.admin.privatemessage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "私聊消息VO")
public class PrivateMessageVO {

    @Schema(description = " 消息id")
    private Long id;

    @Schema(description = "临时id")
    private String tmpId;

    @Schema(description = " 发送者id")
    private Long sendId;

    @Schema(description = " 接收者id")
    private Long recvId;

    @Schema(description = " 发送内容")
    private String content;

    @Schema(description = "消息内容类型 MessageType")
    private Integer type;

    @Schema(description = " 状态")
    private Integer status;

    @Schema(description = " 发送时间")
    private LocalDateTime sendTime;
}
