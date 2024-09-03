package top.hondaman.cloud.infra.asyncImport.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import top.hondaman.cloud.framework.common.pojo.CommonResult;
import top.hondaman.cloud.infra.asyncImport.api.dto.AsyncImportTaskDTO;
import top.hondaman.cloud.infra.asyncImport.api.vo.AsyncImportTaskVO;
import top.hondaman.cloud.infra.enums.ApiConstants;

import javax.validation.Valid;


@FeignClient(name = ApiConstants.NAME,contextId = "AsyncImportTaskApi")
public interface AsyncImportTaskApi {
    String PREFIX = ApiConstants.PREFIX + "/asyncImport/task";

    /**
     * 根据【系统编码+任务编码】获取最近一条导入任务
     * @param systemCode
     * @param taskCode
     * @return
     */
    @GetMapping(PREFIX + "/getLastTask")
    CommonResult<AsyncImportTaskVO> getLastTask(@RequestParam("systemCode") String systemCode, @RequestParam("taskCode") String taskCode);

    /**
     * 初始化一条导入任务
     * @param dto
     * @return
     */
    @PostMapping(PREFIX + "/insertTask")
    CommonResult<String> insertTask(@Valid @RequestBody AsyncImportTaskDTO dto);

    /**
     * 根据主键更新导入任务的状态
     * @param dto
     * @return
     */
    @PostMapping(PREFIX + "/updateTask")
    CommonResult<Boolean> updateTask(@Valid @RequestBody AsyncImportTaskDTO dto);
}
