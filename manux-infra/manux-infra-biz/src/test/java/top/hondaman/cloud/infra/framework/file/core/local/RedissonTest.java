package top.hondaman.cloud.infra.framework.file.core.local;

import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.redisson.connection.balancer.RoundRobinLoadBalancer;

public class RedissonTest {

    @Test
    public void  fun(){
        Config config = new Config();
        config.useMasterSlaveServers()
                .setMasterAddress("redis://192.168.31.98:6379") // 主节点地址
                .addSlaveAddress("redis://192.168.31.98:6380", "redis://192.168.31.98:6381") // 从节点地址
                .setPassword("%Hg!redis2024~IR&^") //设置密码
                .setReadMode(ReadMode.SLAVE) // 设置读操作优先从从节点读取
                .setLoadBalancer(new RoundRobinLoadBalancer()); // 设置负载均衡策略为轮询
        config.setCodec(new StringCodec());// 使用 StringCodec 编解码器

        RedissonClient redisson = Redisson.create(config);


        redisson.getBucket("name").set("ganhua111");

        redisson.shutdown();
    }
}
