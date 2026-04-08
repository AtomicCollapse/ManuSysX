package top.hondaman.manux.module.im.framework.im.annotation;

import org.springframework.stereotype.Component;
import top.hondaman.manux.module.im.enums.IMListenerType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface IMListener {

    IMListenerType type();

}
