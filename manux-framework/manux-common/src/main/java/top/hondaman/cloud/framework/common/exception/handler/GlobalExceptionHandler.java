package top.hondaman.cloud.framework.common.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.hondaman.cloud.framework.common.exception.ServiceException;
import top.hondaman.cloud.framework.common.pojo.CommonResult;

import static top.hondaman.cloud.framework.common.exception.enums.GlobalErrorCodeConstants.BAD_REQUEST;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 业务逻辑错误返回
     * @param ex
     * @return
     */
    @ExceptionHandler(ServiceException.class)
    public CommonResult<?> serviceExceptionHandler(ServiceException ex){
        log.info("[serviceExceptionHandler]", ex);
        return CommonResult.error(ex.getCode(),ex.getMessage());
    }

    /**
     * Validation参数错误返回
     * @param ex
     * @return
     */
    @ExceptionHandler(BindException.class)
    public CommonResult<?> bindExceptionHandler(BindException ex){
        log.info("[bindExceptionHandler]", ex);
        return CommonResult.error(BAD_REQUEST.getCode(),BAD_REQUEST.getMsg()+"["+ex.getAllErrors().get(0).getDefaultMessage()+"]");
    }
}
