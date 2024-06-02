package top.hondaman.cloud.infra.asyncImport.controller;

import org.springframework.web.bind.annotation.*;
import top.hondaman.cloud.framework.common.pojo.CommonResult;
import top.hondaman.cloud.infra.asyncImport.dto.ImportTaskParam;
import top.hondaman.cloud.infra.asyncImport.service.AsyncImportService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/infra/asyncImport")
public class AsyncImportController implements AsyncImportApi{
    @Resource
    private AsyncImportService asyncImportService;

    @Override
    public CommonResult getLastTaskByCode(@RequestBody ImportTaskParam param) {
        return null;
    }

    /**
     * 插入一条导入任务，校验导入配置是否存在
     * @param param
     * @param userId
     * @return
     */
    @Override
    @PostMapping("/insertTask")
    public CommonResult insertTask(@Valid ImportTaskParam param,@RequestHeader("userId") String userId) throws IOException {
        return CommonResult.success(asyncImportService.insertTask(param,userId));
    }
}
