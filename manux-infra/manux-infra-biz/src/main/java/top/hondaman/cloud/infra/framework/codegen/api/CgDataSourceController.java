package top.hondaman.cloud.infra.framework.codegen.api;

import org.springframework.web.bind.annotation.*;
import top.hondaman.cloud.framework.common.pojo.CommonResult;
import top.hondaman.cloud.framework.common.pojo.PageResult;
import top.hondaman.cloud.infra.enums.ApiConstants;
import top.hondaman.cloud.infra.framework.codegen.api.dto.CgDataSourceDTO;
import top.hondaman.cloud.infra.framework.codegen.api.vo.CgDataSourceVO;
import top.hondaman.cloud.infra.framework.codegen.service.CgDataSourceService;
import top.hondaman.cloud.infra.system.model.PageParam;
import top.hondaman.cloud.infra.system.model.UserInfoToken;

import javax.annotation.Resource;
import javax.validation.Valid;

import static top.hondaman.cloud.framework.common.pojo.CommonResult.success;

@RestController
@RequestMapping(ApiConstants.PREFIX + "/codegen/datasource")
public class CgDataSourceController {
    @Resource
    private CgDataSourceService cgDataSourceService;

    @PostMapping("pageList")
    public CommonResult<PageResult<CgDataSourceVO>> getListPage(@RequestBody CgDataSourceDTO param, PageParam pageParam){
        PageResult<CgDataSourceVO> pageResult = cgDataSourceService.getListPage(param,pageParam);
        return success(pageResult);
    }

    @PostMapping("insert")
    public CommonResult<String> insert(@Valid @RequestBody CgDataSourceDTO param, UserInfoToken userInfo){
        return success(cgDataSourceService.insert(param,userInfo));
    }
}
