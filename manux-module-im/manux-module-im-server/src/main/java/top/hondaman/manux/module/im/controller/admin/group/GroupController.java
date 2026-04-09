package top.hondaman.manux.module.im.controller.admin.group;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hondaman.manux.framework.common.pojo.CommonResult;
import top.hondaman.manux.framework.idempotent.core.annotation.Idempotent;
import top.hondaman.manux.module.im.api.group.dto.GroupDndDTO;
import top.hondaman.manux.module.im.api.group.dto.GroupInviteDTO;
import top.hondaman.manux.module.im.api.group.dto.GroupMemberRemoveDTO;
import top.hondaman.manux.module.im.controller.admin.group.vo.GroupMemberVO;
import top.hondaman.manux.module.im.controller.admin.group.vo.GroupVO;
import top.hondaman.manux.module.im.service.group.GroupService;

import java.util.List;

import static top.hondaman.manux.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - IM群聊")
@RestController
@RequestMapping("/im/group")
@Validated
public class GroupController {

    @Resource
    private GroupService groupService;

    @Idempotent
    @Operation(summary = "创建群聊", description = "创建群聊")
    @PostMapping("/create")
    public CommonResult<GroupVO> createGroup(@Valid @RequestBody GroupVO vo) {
        return success(groupService.createGroup(vo));
    }

    @Idempotent
    @Operation(summary = "修改群聊信息", description = "修改群聊信息")
    @PutMapping("/modify")
    public CommonResult<GroupVO> modifyGroup(@Valid @RequestBody GroupVO vo) {
        return success(groupService.modifyGroup(vo));
    }

    @Idempotent
    @Operation(summary = "解散群聊", description = "解散群聊")
    @DeleteMapping("/delete/{groupId}")
    public CommonResult<Boolean> deleteGroup(@NotNull(message = "群聊id不能为空") @PathVariable Long groupId) {
        groupService.deleteGroup(groupId);
        return success(true);
    }


    @Operation(summary = "查询群聊", description = "查询单个群聊信息")
    @GetMapping("/find/{groupId}")
    public CommonResult<GroupVO> findGroup(@NotNull(message = "群聊id不能为空") @PathVariable Long groupId) {
        return success(groupService.findById(groupId));
    }

    @Operation(summary = "查询群聊列表", description = "查询群聊列表")
    @GetMapping("/list")
    public CommonResult<List<GroupVO>> findGroups() {
        return success(groupService.findGroups());
    }

    @Idempotent
    @Operation(summary = "邀请进群", description = "邀请好友进群")
    @PostMapping("/invite")
    public CommonResult<Boolean> invite(@Valid @RequestBody GroupInviteDTO dto) {
        groupService.invite(dto);
        return success(true);
    }

    @Operation(summary = "查询群聊成员", description = "查询群聊成员")
    @GetMapping("/members/{groupId}")
    public CommonResult<List<GroupMemberVO>> findGroupMembers(
            @NotNull(message = "群聊id不能为空") @PathVariable Long groupId) {
        return success(groupService.findGroupMembers(groupId));
    }

    @Idempotent
    @Operation(summary = "将成员移出群聊", description = "将成员移出群聊")
    @DeleteMapping("/members/remove")
    public CommonResult<Boolean> removeMembers(@NotNull(message = "群id不可为空") @RequestParam Long groupId,
                                               @NotEmpty(message = "成员用户id不可为空") @RequestParam List<Long> userIds) {
        groupService.removeGroupMembers(new GroupMemberRemoveDTO().setGroupId(groupId).setUserIds(userIds));
        return success(true);
    }


    @Idempotent
    @Operation(summary = "退出群聊", description = "退出群聊")
    @DeleteMapping("/quit/{groupId}")
    public CommonResult<Boolean> quitGroup(@NotNull(message = "群聊id不能为空") @PathVariable Long groupId) {
        groupService.quitGroup(groupId);
        return success(true);
    }

    @Idempotent
    @Operation(summary = "踢出群聊(已废弃)", description = "将用户踢出群聊")
    @DeleteMapping("/kick/{groupId}")
    public CommonResult<Boolean> kickGroup(@NotNull(message = "群聊id不能为空") @PathVariable Long groupId,
                            @NotNull(message = "用户id不能为空") @RequestParam Long userId) {
        groupService.kickGroup(groupId, userId);
        return success(true);
    }

    @Operation(summary = "开启/关闭免打扰", description = "开启/关闭免打扰")
    @PutMapping("/dnd")
    public CommonResult<Boolean> setGroupDnd(@Valid @RequestBody GroupDndDTO dto) {
        groupService.setDnd(dto);
        return success(true);
    }
}