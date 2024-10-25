package top.hondaman.cloud.infra.framework.codegen.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hondaman.cloud.framework.common.pojo.CommonResult;
import top.hondaman.cloud.framework.common.pojo.PageResult;
import top.hondaman.cloud.infra.enums.ApiConstants;
import top.hondaman.cloud.infra.framework.codegen.api.dto.CgModuleDTO;
import top.hondaman.cloud.infra.framework.codegen.api.vo.CgModuleVO;
import top.hondaman.cloud.infra.framework.codegen.service.CgModuleService;
import top.hondaman.cloud.infra.system.model.PageParam;
import top.hondaman.cloud.infra.system.model.UserInfoToken;

import javax.annotation.Resource;
import javax.validation.Valid;

import static top.hondaman.cloud.framework.common.pojo.CommonResult.success;

@RestController
@RequestMapping(ApiConstants.PREFIX + "/codegen/module")
public class CgModuleController {
    @Resource
    private CgModuleService cgModuleService;

    @PostMapping("pageList")
    public CommonResult<PageResult<CgModuleVO>> getListPage(@RequestBody CgModuleDTO param, PageParam pageParam){
        PageResult<CgModuleVO> pageResult = cgModuleService.getListPage(param,pageParam);
        return success(pageResult);
    }

    @PostMapping("insert")
    public CommonResult<String> insert(@Valid @RequestBody CgModuleDTO param, UserInfoToken userInfo){
        return success(cgModuleService.insert(param,userInfo));
    }
}
