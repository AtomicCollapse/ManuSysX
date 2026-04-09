package top.hondaman.manux.module.im.controller.admin.friend;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hondaman.manux.framework.common.pojo.CommonResult;
import top.hondaman.manux.framework.idempotent.core.annotation.Idempotent;
import top.hondaman.manux.framework.security.core.util.SecurityFrameworkUtils;
import top.hondaman.manux.module.im.api.friend.dto.FriendDndDTO;
import top.hondaman.manux.module.im.controller.admin.friend.vo.FriendVO;
import top.hondaman.manux.module.im.service.friend.FriendService;

import java.util.List;

import static top.hondaman.manux.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - IM好友")
@RestController
@RequestMapping("/im/friend")
@Validated
public class FriendController {

    @Resource
    private FriendService friendService;

    @GetMapping("/list")
    @Operation(summary = "好友列表", description = "获取好友列表")
    public CommonResult<List<FriendVO>> findFriends() {
        return success(friendService.findFriends());
    }

    @Idempotent
    @PostMapping("/add")
    @Operation(summary = "添加好友", description = "双方建立好友关系")
    public CommonResult<Boolean> addFriend(@NotNull(message = "好友id不可为空") @RequestParam Long friendId) {
        friendService.addFriend(SecurityFrameworkUtils.getLoginImUserId(),friendId);
        return success(true);
    }

    @GetMapping("/find/{friendId}")
    @Operation(summary = "查找好友信息", description = "查找好友信息")
    public CommonResult<FriendVO> findFriend(@NotNull(message = "好友id不可为空") @PathVariable Long friendId) {
        return success(friendService.findFriend(friendId));
    }

    @DeleteMapping("/delete/{friendId}")
    @Operation(summary = "删除好友", description = "解除好友关系")
    public CommonResult<Boolean> delFriend(@NotNull(message = "好友id不可为空") @PathVariable Long friendId) {
        friendService.delFriend(friendId);
        return success(true);
    }

    @PutMapping("/dnd")
    @Operation(summary = "开启/关闭免打扰状态", description = "开启/关闭免打扰状态")
    public CommonResult<Boolean> setFriendDnd(@Valid @RequestBody FriendDndDTO dto) {
        friendService.setDnd(dto);
        return success(true);
    }
}
