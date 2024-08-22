package top.hondaman.cloud.infra.asyncImport.controller;

import org.springframework.web.bind.annotation.*;
import top.hondaman.cloud.framework.common.pojo.CommonResult;
import top.hondaman.cloud.infra.asyncImport.controller.dto.AsyncImportTaskDTO;
import top.hondaman.cloud.infra.asyncImport.controller.vo.AsyncImportTaskVO;
import top.hondaman.cloud.infra.asyncImport.service.AsyncImportTaskService;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("asyncImport/task")
public class AsyncImportTaskController {
    @Resource
    private AsyncImportTaskService service;

    /**
     * 根据【系统编码+任务编码】获取最近一条导入任务
     * @param systemCode
     * @param taskCode
     * @return
     */
    @GetMapping("getLastTask")
    public CommonResult<AsyncImportTaskVO> getLastTask(@RequestParam String systemCode,@RequestParam String taskCode){
        return CommonResult.success(service.getLastTask(systemCode,taskCode));
    }

    /**
     * 初始化一条导入任务
     * @param dto
     * @return
     */
    @PostMapping("insertTask")
    public CommonResult<String> insertTask(@Valid @RequestBody AsyncImportTaskDTO dto){
        return CommonResult.success(service.insertTask(dto));
    }

    /**
     * 根据主键更新导入任务的状态
     * @param dto
     * @return
     */
    @PostMapping("updateTask")
    public CommonResult<Boolean> updateTask(@Valid @RequestBody AsyncImportTaskDTO dto){
        service.updateTask(dto);
        return CommonResult.success(true);
    }
}
