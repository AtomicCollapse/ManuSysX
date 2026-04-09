package top.hondaman.manux.module.im.framework.redis.mq.core;

import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.hondaman.manux.module.im.util.ThreadPoolExecutorFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * reids 队列拉取定时任务
 */
@Slf4j
@Component
public class RedisMQPullTask implements CommandLineRunner {

    private String version = Strings.EMPTY;
    private static final ScheduledThreadPoolExecutor EXECUTOR = ThreadPoolExecutorFactory.getThreadPoolExecutor();

    @Autowired(required = false)
    private List<RedisMQConsumer> consumers = Collections.emptyList();

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void run(String... args) {
        consumers.forEach((consumer -> {
            // 注解参数
            RedisMQListener annotation = consumer.getClass().getAnnotation(RedisMQListener.class);
            String queue = annotation.queue();
            int batchSize = annotation.batchSize();
            int period = annotation.period();
            // 获取泛型类型
            Type superClass = consumer.getClass().getGenericSuperclass();
            Type type = ((ParameterizedType)superClass).getActualTypeArguments()[0];
            EXECUTOR.execute(new Runnable() {
                @Override
                public void run() {
                    List<Object> datas = new LinkedList<>();
                    try {
                        if (consumer.isReady()) {
                            String key = consumer.generateKey();
                            // 拉取一个批次的数据
                            List<Object> objects = pullBatch(key, batchSize);
                            for (Object obj : objects) {
                                if (obj instanceof JSONObject) {
                                    JSONObject jsonObject = (JSONObject)obj;
                                    Object data = jsonObject.toJavaObject(type);
                                    consumer.onMessage(data);
                                    datas.add(data);
                                }
                            }
                            if (!datas.isEmpty()) {
                                consumer.onMessage(datas);
                            }
                        }
                    } catch (Exception e) {
                        log.error("数据消费异常,队列:{}", queue, e);
                        // 出现异常，10s后再重新尝试消费
                        EXECUTOR.schedule(this, 10, TimeUnit.SECONDS);
                        return;
                    }
                    // 继续消费数据
                    if (!EXECUTOR.isShutdown()) {
                        if (datas.size() < batchSize) {
                            // 数据已经消费完，等待下一个周期继续拉取
                            EXECUTOR.schedule(this, period, TimeUnit.MILLISECONDS);
                        } else {
                            // 数据没有消费完，直接开启下一个消费周期
                            EXECUTOR.execute(this);
                        }
                    }
                }
            });
        }));
    }

    private List<Object> pullBatch(String key, Integer batchSize) {
        List<Object> objects = new LinkedList<>();
        if (isSupportBatchPull()) {
            // 版本大于6.2，支持批量拉取
            objects = redisTemplate.opsForList().leftPop(key, batchSize);
        } else {
            // 版本小于6.2，只能逐条拉取
            Object obj = redisTemplate.opsForList().leftPop(key);
            while (!Objects.isNull(obj) && objects.size() < batchSize) {
                objects.add(obj);
                obj = redisTemplate.opsForList().leftPop(key);
            }
            if (!Objects.isNull(obj)){
                objects.add(obj);
            }
        }
        return objects;
    }

    @PreDestroy
    public void destory() {
        log.info("消费线程停止...");
        ThreadPoolExecutorFactory.shutDown();
    }

    private String getVersion() {
        if (version.isEmpty()) {
            RedisConnection connection = RedisConnectionUtils.getConnection(redisTemplate.getConnectionFactory());
            Properties properties = connection.info();
            for (String key : properties.stringPropertyNames()) {
                if (key.contains("redis_version")) {
                    version = properties.getProperty(key);
                    break;
                }
            }
            RedisConnectionUtils.releaseConnection(connection,redisTemplate.getConnectionFactory());
        }
        return version;
    }

    /**
     * 是否支持批量拉取，redis版本大于6.2支持批量拉取
     * @return
     */
    private Boolean isSupportBatchPull() {
        String version = getVersion();
        String[] arr = version.split("\\.");
        if (arr.length < 2) {
            return false;
        }
        Integer firVersion = Integer.valueOf(arr[0]);
        Integer secVersion = Integer.valueOf(arr[1]);
        return firVersion > 6 || (firVersion == 6 && secVersion >= 2);
    }
}
