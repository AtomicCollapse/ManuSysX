package top.hondaman.cloud.framework.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.hondaman.cloud.framework.web.core.TokenUserHandlerMethodArgumentResolver;

import java.util.List;


/**
 * 对MVC进行自定义配置有两种常用方法
 * 1、WebMvcConfigurer接口，主要用于添加或修改Spring MVC的配置，如添加拦截器，自定义消息转换器等。
 * 2、WebMvcConfigurationSupport，主要用于完全自定义Spring MVC的配置，如果我们需要对Spring MVC的配置进行大量的自定义，可以选择继承该类并重写其中的方法。但是需要注意的是，继承该类会覆盖Spring MVC的部分默认配置
 *
 * 因此，当我们只需要对部分配置进行自定义时，应该使用WebMvcConfigurer
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        //注册自定义参数解析器
        resolvers.add(new TokenUserHandlerMethodArgumentResolver());
    }
}
