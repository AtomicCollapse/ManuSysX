package top.hondaman.cloud.framework.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.hondaman.cloud.framework.common.exception.enums.GlobalErrorCodeConstants;

/**
 * 业务逻辑异常 Exception
 */
@Data
@EqualsAndHashCode(callSuper = true)
public final class ServiceException extends RuntimeException {
    /**
     * 全局错误码
     *
     * @see GlobalErrorCodeConstants
     */
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 空构造方法，避免反序列化问题
     */
    public ServiceException(){

    }

    public ServiceException(Integer code,String message){
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
