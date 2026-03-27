package top.hondaman.manux.framework.idempotent.config;

import top.hondaman.manux.framework.idempotent.core.aop.IdempotentAspect;
import top.hondaman.manux.framework.idempotent.core.keyresolver.impl.DefaultIdempotentKeyResolver;
import top.hondaman.manux.framework.idempotent.core.keyresolver.impl.ExpressionIdempotentKeyResolver;
import top.hondaman.manux.framework.idempotent.core.keyresolver.IdempotentKeyResolver;
import top.hondaman.manux.framework.idempotent.core.keyresolver.impl.UserIdempotentKeyResolver;
import top.hondaman.manux.framework.idempotent.core.redis.IdempotentRedisDAO;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import top.hondaman.manux.framework.redis.config.ManuxRedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@AutoConfiguration(after = ManuxRedisAutoConfiguration.class)
public class ManuxIdempotentConfiguration {

    @Bean
    public IdempotentAspect idempotentAspect(List<IdempotentKeyResolver> keyResolvers, IdempotentRedisDAO idempotentRedisDAO) {
        return new IdempotentAspect(keyResolvers, idempotentRedisDAO);
    }

    @Bean
    public IdempotentRedisDAO idempotentRedisDAO(StringRedisTemplate stringRedisTemplate) {
        return new IdempotentRedisDAO(stringRedisTemplate);
    }

    // ========== 各种 IdempotentKeyResolver Bean ==========

    @Bean
    public DefaultIdempotentKeyResolver defaultIdempotentKeyResolver() {
        return new DefaultIdempotentKeyResolver();
    }

    @Bean
    public UserIdempotentKeyResolver userIdempotentKeyResolver() {
        return new UserIdempotentKeyResolver();
    }

    @Bean
    public ExpressionIdempotentKeyResolver expressionIdempotentKeyResolver() {
        return new ExpressionIdempotentKeyResolver();
    }

}
