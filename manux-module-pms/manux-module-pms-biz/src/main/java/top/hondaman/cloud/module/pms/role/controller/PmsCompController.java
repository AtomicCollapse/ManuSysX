package top.hondaman.cloud.module.pms.role.controller;

import org.springframework.web.bind.annotation.*;
import top.hondaman.cloud.framework.common.pojo.CommonResult;
import top.hondaman.cloud.framework.common.pojo.PageResult;
import top.hondaman.cloud.module.pms.role.dto.PmsCompDto;
import top.hondaman.cloud.module.pms.role.dto.PmsCompParam;
import top.hondaman.cloud.module.pms.role.service.PmsCompService;

import javax.annotation.Resource;

import static top.hondaman.cloud.framework.common.pojo.CommonResult.success;

@RestController
@RequestMapping("/pms/comp")
public class PmsCompController {
    @Resource
    private PmsCompService pmsCompService;

    @GetMapping("pageList")
    public CommonResult<PageResult<PmsCompDto>> getListPage(@RequestBody PmsCompParam param){
        PageResult<PmsCompDto> pageResult = pmsCompService.getListPage(param);
        return success(pageResult);
    }
}
