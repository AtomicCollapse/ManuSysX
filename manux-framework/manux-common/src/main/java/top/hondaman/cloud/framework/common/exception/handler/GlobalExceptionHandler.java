package top.hondaman.cloud.framework.common.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.hondaman.cloud.framework.common.exception.ServiceException;
import top.hondaman.cloud.framework.common.pojo.CommonResult;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public CommonResult<?> serviceExceptionHandler(ServiceException ex){
        log.info("[serviceExceptionHandler]", ex);
        return CommonResult.error(ex.getCode(),ex.getMessage());
    }
}
