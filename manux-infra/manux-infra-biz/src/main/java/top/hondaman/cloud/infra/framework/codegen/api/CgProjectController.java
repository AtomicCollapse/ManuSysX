package top.hondaman.cloud.infra.framework.codegen.api;

import org.springframework.web.bind.annotation.*;
import top.hondaman.cloud.framework.common.pojo.CommonResult;
import top.hondaman.cloud.framework.common.pojo.PageResult;
import top.hondaman.cloud.infra.enums.ApiConstants;
import top.hondaman.cloud.infra.framework.codegen.api.dto.CgProjectDTO;
import top.hondaman.cloud.infra.framework.codegen.api.vo.CgProjectVO;
import top.hondaman.cloud.infra.framework.codegen.service.CgProjectService;
import top.hondaman.cloud.infra.system.model.PageParam;
import top.hondaman.cloud.infra.system.model.UserInfoToken;

import javax.annotation.Resource;
import javax.validation.Valid;

import static top.hondaman.cloud.framework.common.pojo.CommonResult.success;

@RestController
@RequestMapping(ApiConstants.PREFIX + "/codegen/project")
public class CgProjectController {
    @Resource
    private CgProjectService cgProjectService;

    @PostMapping("pageList")
    public CommonResult<PageResult<CgProjectVO>> getListPage(@RequestBody CgProjectDTO param, PageParam pageParam, UserInfoToken userInfo){
        PageResult<CgProjectVO> pageResult = cgProjectService.getListPage(param,pageParam);
        return success(pageResult);
    }

    @PostMapping("insert")
    public CommonResult<String> insert(@Valid @RequestBody CgProjectDTO param, UserInfoToken userInfo){
        return success(cgProjectService.insert(param,userInfo));
    }
}
