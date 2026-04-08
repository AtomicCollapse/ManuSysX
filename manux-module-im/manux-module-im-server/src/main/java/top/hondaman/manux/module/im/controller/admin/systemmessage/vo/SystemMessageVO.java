package top.hondaman.manux.module.im.controller.admin.systemmessage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "系统消息VO")
public class SystemMessageVO {

    @Schema(description = " 消息id")
    private Long id;

    @Schema(description = " 发送内容")
    private String content;

    @Schema(description = "消息内容类型 MessageType")
    private Integer type;

    @Schema(description = " 发送时间")
    private LocalDateTime sendTime;
}
