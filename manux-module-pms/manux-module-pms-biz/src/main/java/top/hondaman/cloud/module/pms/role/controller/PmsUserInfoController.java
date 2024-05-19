package top.hondaman.cloud.module.pms.role.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hondaman.cloud.framework.common.pojo.CommonResult;
import top.hondaman.cloud.framework.redis.oauth2.dto.OAuth2AccessTokenDto;
import top.hondaman.cloud.infra.system.model.UserInfoToken;
import top.hondaman.cloud.module.pms.role.dto.PmsUserInfoParam;
import top.hondaman.cloud.module.pms.role.service.PmsUserInfoService;

import javax.annotation.Resource;
import javax.validation.Valid;

import static top.hondaman.cloud.framework.common.pojo.CommonResult.success;

@RestController
@Validated
@RequestMapping("/pms/userInfo")
public class PmsUserInfoController {
    @Resource
    private PmsUserInfoService service;

    @PostMapping("insert")
    public CommonResult<String> insert(@Valid @RequestBody PmsUserInfoParam param, @RequestHeader String userId){
        return success(service.insert(param,userId));
    }

    @PostMapping("update")
    public CommonResult<Boolean> update(@Valid @RequestBody PmsUserInfoParam param,@RequestHeader String userId){
        service.update(param,userId);
        return success(true);
    }

    /**
     * 登录接口，成功返回tokenId
     * @param param
     * @return
     */
    @PostMapping("login")
    public CommonResult<String> login(@Valid @RequestBody PmsUserInfoParam param){

        return success(service.doLogin(param));
    }

    @PostMapping("logout")
    public CommonResult<OAuth2AccessTokenDto> logout(UserInfoToken userInfoToken){
        return success(service.doLogout(userInfoToken));
    }
}
