package top.hondaman.manux.module.im.framework.im.aop;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import top.hondaman.manux.framework.security.core.util.SecurityFrameworkUtils;
import top.hondaman.manux.module.im.framework.im.core.IMClient;

import static top.hondaman.manux.framework.common.exception.util.ServiceExceptionUtil.exception;
import static top.hondaman.manux.module.im.enums.ErrorCodeConstants.IM_OFFLINE;

/**
 * @author: blue
 * @date: 2024-06-16
 * @version: 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OnlineCheckAspect {

    private final IMClient imClient;

    @Around("@annotation(com.bx.implatform.annotation.OnlineCheck)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Long loginUserId = SecurityFrameworkUtils.getLoginImUserId();
        if(!imClient.isOnline(loginUserId)){
            throw exception(IM_OFFLINE);
        }
        return joinPoint.proceed();
    }

}
