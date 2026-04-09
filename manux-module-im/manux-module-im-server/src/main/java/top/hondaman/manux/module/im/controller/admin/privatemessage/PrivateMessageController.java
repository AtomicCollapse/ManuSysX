package top.hondaman.manux.module.im.controller.admin.privatemessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hondaman.manux.framework.common.pojo.CommonResult;
import top.hondaman.manux.module.im.api.privatemessage.dto.PrivateMessageDTO;
import top.hondaman.manux.module.im.controller.admin.privatemessage.vo.PrivateMessageVO;
import top.hondaman.manux.module.im.service.privatemessage.PrivateMessageService;

import java.util.List;

import static top.hondaman.manux.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - IM私聊消息")
@RestController
@RequestMapping("/im/message/private")
@Validated
public class PrivateMessageController {

    @Resource
    private PrivateMessageService privateMessageService;

    @PostMapping("/send")
    @Operation(summary = "发送消息", description = "发送私聊消息")
    public CommonResult<PrivateMessageVO> sendMessage(@Valid @RequestBody PrivateMessageDTO vo) {
        return success(privateMessageService.sendMessage(vo));
    }

    @DeleteMapping("/recall/{id}")
    @Operation(summary = "撤回消息", description = "撤回私聊消息")
    public CommonResult<PrivateMessageVO> recallMessage(@NotNull(message = "消息id不能为空") @PathVariable Long id) {
        return success( privateMessageService.recallMessage(id));
    }

    @GetMapping("/pullOfflineMessage")
    @Operation(summary = "拉取离线消息(已废弃)", description = "拉取离线消息,消息将通过webscoket异步推送")
    public CommonResult<Boolean> pullOfflineMessage(@RequestParam Long minId) {
        privateMessageService.pullOfflineMessage(minId);
        return success(true);
    }

    @GetMapping(value = "/loadOfflineMessage")
    @Operation(summary = "拉取离线消息", description = "拉取离线消息")
    public CommonResult<List<PrivateMessageVO>> loadOfflineMessage(@RequestParam Long minId) {
        return success(privateMessageService.loadOfflineMessage(minId));
    }


    @PutMapping("/readed")
    @Operation(summary = "消息已读", description = "将会话中接收的消息状态置为已读")
    public CommonResult<Boolean> readedMessage(@RequestParam Long friendId) {
        privateMessageService.readedMessage(friendId);
        return success(true);
    }

    @GetMapping("/maxReadedId")
    @Operation(summary = "获取最大已读消息的id", description = "获取某个会话中已读消息的最大id")
    public CommonResult<Long> getMaxReadedId(@RequestParam Long friendId) {
        return success(privateMessageService.getMaxReadedId(friendId));
    }

    @GetMapping("/history")
    @Operation(summary = "查询聊天记录", description = "查询聊天记录")
    public CommonResult<List<PrivateMessageVO>> recallMessage(
            @NotNull(message = "好友id不能为空") @RequestParam Long friendId,
            @NotNull(message = "页码不能为空") @RequestParam Long page,
            @NotNull(message = "size不能为空") @RequestParam Long size) {
        return success(privateMessageService.findHistoryMessage(friendId, page, size));
    }
}