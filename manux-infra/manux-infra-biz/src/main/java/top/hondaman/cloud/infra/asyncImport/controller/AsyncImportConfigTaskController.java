package top.hondaman.cloud.infra.asyncImport.controller;

import org.springframework.web.bind.annotation.*;

import top.hondaman.cloud.framework.common.pojo.CommonResult;
import top.hondaman.cloud.infra.asyncImport.controller.dto.AsyncImportConfigDTO;
import top.hondaman.cloud.infra.asyncImport.controller.vo.AsyncImportConfigVO;
import top.hondaman.cloud.infra.asyncImport.service.AsyncImportConfigService;

import javax.annotation.Resource;
import javax.validation.Valid;


@RestController
@RequestMapping("asyncImport/config")
public class AsyncImportConfigTaskController {
    @Resource
    private AsyncImportConfigService service;

    /**
     * 根据【系统编码+任务编码】获取导入配置
     * @param systemCode
     * @param taskCode
     * @return
     */
    @GetMapping("getConfigByCode")
    public CommonResult<AsyncImportConfigVO> getConfigByCode(@RequestParam String systemCode,@RequestParam String taskCode){
        return CommonResult.success(service.getConfigByCode(systemCode,taskCode));
    }

    /**
     * 新增一条导入配置
     * @param dto
     * @return
     */
    @PostMapping("insertConfig")
    public CommonResult<String> insertConfig(@Valid @RequestBody AsyncImportConfigDTO dto){
        return CommonResult.success(service.insertConfig(dto));
    }
}
