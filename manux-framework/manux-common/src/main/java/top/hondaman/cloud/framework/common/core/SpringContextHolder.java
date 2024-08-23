package top.hondaman.cloud.framework.common.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext(){
        assertApplicationContext();
        return SpringContextHolder.applicationContext;
    }

    public static <T> T getBean(String beanName){
        assertApplicationContext();
        return (T)SpringContextHolder.applicationContext.getBean(beanName);
    }

    public static <T> T getBean(Class<T> typeName){
        assertApplicationContext();
        return applicationContext.getBean(typeName);
    }

    private static void assertApplicationContext(){
        if(applicationContext == null){
            throw new RuntimeException("applicationContext对象不该为空而为空,故而抛出异常");
        }
    }
}
