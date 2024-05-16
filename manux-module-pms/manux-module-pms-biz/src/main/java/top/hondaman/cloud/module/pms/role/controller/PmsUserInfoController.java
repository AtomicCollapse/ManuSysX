package top.hondaman.cloud.module.pms.role.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import top.hondaman.cloud.framework.common.pojo.CommonResult;
import top.hondaman.cloud.module.pms.role.dto.PmsUserInfoParam;
import top.hondaman.cloud.module.pms.role.service.PmsUserInfoService;

import javax.annotation.Resource;
import javax.validation.Valid;

import static top.hondaman.cloud.framework.common.pojo.CommonResult.success;

@Controller
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
}
