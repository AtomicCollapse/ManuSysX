package top.hondaman.manux.module.im.framework.im.core.task;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import top.hondaman.manux.module.im.framework.redis.mq.core.RedisMQConsumer;

@Slf4j
public abstract class AbstractMessageResultTask<T> extends RedisMQConsumer<T> {

    @Value("${spring.application.name}")
    private String appName;

    @Override
    public String generateKey() {
        return StrUtil.join(":", super.generateKey(), appName);
    }



}
