package top.hondaman.manux.module.im.controller.admin.groupmessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hondaman.manux.framework.common.pojo.CommonResult;
import top.hondaman.manux.module.im.api.groupmessage.dto.GroupMessageDTO;
import top.hondaman.manux.module.im.service.groupmessage.GroupMessageService;
import top.hondaman.manux.module.im.controller.admin.groupmessage.vo.GroupMessageVO;
import java.util.List;

import static top.hondaman.manux.framework.common.pojo.CommonResult.success;


@Tag(name = "管理后台 - IM群消息")
@RestController
@RequestMapping("/im/message/group")
@Validated
public class GroupMessageController {

    @Resource
    private GroupMessageService groupMessageService;

    @PostMapping("/send")
    @Operation(summary = "发送群聊消息", description = "发送群聊消息")
    public CommonResult<GroupMessageVO> sendMessage(@Valid @RequestBody GroupMessageDTO vo) {
        return success(groupMessageService.sendMessage(vo));
    }

    @DeleteMapping("/recall/{id}")
    @Operation(summary = "撤回消息", description = "撤回群聊消息")
    public CommonResult<GroupMessageVO> recallMessage(@NotNull(message = "消息id不能为空") @PathVariable Long id) {
        return success(groupMessageService.recallMessage(id));
    }

    @GetMapping("/pullOfflineMessage")
    @Operation(summary = "拉取离线消息(已废弃)", description = "拉取离线消息,消息将通过webscoket异步推送")
    public CommonResult<Boolean> pullOfflineMessage(@RequestParam Long minId) {
        groupMessageService.pullOfflineMessage(minId);
        return success(true);
    }

    @GetMapping(value = "/loadOfflineMessage")
    @Operation(summary = "拉取离线消息", description = "拉取离线消息")
    public CommonResult<List<GroupMessageVO>> loadOfflineMessage(@RequestParam Long minId) {
        return success(groupMessageService.loadOffineMessage(minId));
    }


    @PutMapping("/readed")
    @Operation(summary = "消息已读", description = "将群聊中的消息状态置为已读")
    public CommonResult<Boolean> readedMessage(@RequestParam Long groupId) {
        groupMessageService.readedMessage(groupId);
        return success(true);
    }

    @GetMapping("/findReadedUsers")
    @Operation(summary = "获取已读用户id", description = "获取消息已读用户列表")
    public CommonResult<List<Long>> findReadedUsers(@RequestParam Long groupId,
                                              @RequestParam Long messageId) {
        return success(groupMessageService.findReadedUsers(groupId, messageId));
    }

    @GetMapping("/history")
    @Operation(summary = "查询聊天记录", description = "查询聊天记录")
    public CommonResult<List<GroupMessageVO>> recallMessage(@NotNull(message = "群聊id不能为空") @RequestParam Long groupId,
                                                      @NotNull(message = "页码不能为空") @RequestParam Long page,
                                                      @NotNull(message = "size不能为空") @RequestParam Long size) {
        return success(groupMessageService.findHistoryMessage(groupId, page, size));
    }
}