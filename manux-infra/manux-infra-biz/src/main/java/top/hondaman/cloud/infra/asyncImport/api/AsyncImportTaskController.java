package top.hondaman.cloud.infra.asyncImport.api;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hondaman.cloud.framework.common.pojo.CommonResult;
import top.hondaman.cloud.infra.asyncImport.api.dto.AsyncImportTaskDTO;
import top.hondaman.cloud.infra.asyncImport.api.vo.AsyncImportTaskVO;
import top.hondaman.cloud.infra.asyncImport.service.AsyncImportTaskService;
import top.hondaman.cloud.infra.enums.ApiConstants;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@Validated
public class AsyncImportTaskController implements AsyncImportTaskApi{
    @Resource
    private AsyncImportTaskService service;

    @Override
    public CommonResult<AsyncImportTaskVO> getLastTask(@RequestParam String systemCode,@RequestParam String taskCode){
        return CommonResult.success(service.getLastTask(systemCode,taskCode));
    }


    @Override
    public CommonResult<String> insertTask(@Valid @RequestBody AsyncImportTaskDTO dto){
        return CommonResult.success(service.insertTask(dto));
    }

    @Override
    public CommonResult<Boolean> updateTask(@Valid @RequestBody AsyncImportTaskDTO dto){
        service.updateTask(dto);
        return CommonResult.success(true);
    }
}
