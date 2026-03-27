package top.hondaman.manux.module.infra.api.logger;

import top.hondaman.manux.framework.common.biz.infra.logger.ApiErrorLogCommonApi;
import top.hondaman.manux.framework.common.biz.infra.logger.dto.ApiErrorLogCreateReqDTO;
import top.hondaman.manux.framework.common.pojo.CommonResult;
import top.hondaman.manux.module.infra.service.logger.ApiErrorLogService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

import static top.hondaman.manux.framework.common.pojo.CommonResult.success;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class ApiErrorLogApiImpl implements ApiErrorLogCommonApi {

    @Resource
    private ApiErrorLogService apiErrorLogService;

    @Override
    public CommonResult<Boolean> createApiErrorLog(ApiErrorLogCreateReqDTO createDTO) {
        apiErrorLogService.createApiErrorLog(createDTO);
        return success(true);
    }

}
