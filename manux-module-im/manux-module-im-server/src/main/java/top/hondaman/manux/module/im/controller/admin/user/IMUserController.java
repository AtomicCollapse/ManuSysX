package top.hondaman.manux.module.im.controller.admin.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hondaman.manux.framework.common.pojo.CommonResult;
import top.hondaman.manux.framework.common.util.object.BeanUtils;
import top.hondaman.manux.framework.security.core.util.SecurityFrameworkUtils;
import top.hondaman.manux.module.im.api.user.dto.RegisterDTO;
import top.hondaman.manux.module.im.controller.admin.user.vo.IMUserVO;
import top.hondaman.manux.module.im.controller.admin.user.vo.OnlineTerminalVO;
import top.hondaman.manux.module.im.dal.dataobject.user.IMUserDO;
import top.hondaman.manux.module.im.service.user.UserService;

import java.util.List;

import static top.hondaman.manux.framework.common.pojo.CommonResult.success;


@Tag(name = "管理后台 - IM用户")
@RestController
@RequestMapping("/im/user")
@Validated
public class IMUserController {

    @Resource
    private UserService userService;

    // TODO 这个接口要移到api里去
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "用户注册")
    public CommonResult<Boolean> register(@Valid @RequestBody RegisterDTO dto) {
        userService.register(dto);
        return success(true);
    }


    @GetMapping("/terminal/online")
    @Operation(summary = "判断用户哪个终端在线", description = "返回在线的用户id的终端集合")
    public CommonResult<List<OnlineTerminalVO>> getOnlineTerminal(@NotNull @RequestParam("userIds") String userIds) {
        return success(userService.getOnlineTerminals(userIds));
    }


    @GetMapping("/self")
    @Operation(summary = "获取当前用户信息", description = "获取当前用户信息")
    public CommonResult<IMUserVO> findSelfInfo() {
        IMUserDO user = userService.getById(SecurityFrameworkUtils.getLoginImUserId());
        IMUserVO userVO = BeanUtils.toBean(user, IMUserVO.class);
        return success(userVO);
    }


    @GetMapping("/find/{id}")
    @Operation(summary = "查找用户", description = "根据id查找用户")
    public CommonResult<IMUserVO> findById(@NotNull @PathVariable("id") Long id) {
        return success(userService.findUserById(id));
    }

    @PutMapping("/updateBySelf")
    @Operation(summary = "修改用户信息", description = "修改用户信息，仅允许修改登录用户信息")
    public CommonResult<Boolean> update(@Valid @RequestBody IMUserVO vo) {
        userService.update(vo);
        return success(true);
    }

    @GetMapping("/findByName")
    @Operation(summary = "查找用户", description = "根据用户名或昵称查找用户")
    public CommonResult<List<IMUserVO>> findByName(@RequestParam String name) {
        return success(userService.findUserByName(name));
    }
}