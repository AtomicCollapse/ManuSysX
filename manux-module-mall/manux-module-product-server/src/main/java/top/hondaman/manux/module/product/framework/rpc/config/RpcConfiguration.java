package top.hondaman.manux.module.product.framework.rpc.config;

import top.hondaman.manux.module.member.api.level.MemberLevelApi;
import top.hondaman.manux.module.member.api.user.MemberUserApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "productRpcConfiguration", proxyBeanMethods = false)
@EnableFeignClients(clients = {MemberUserApi.class, MemberLevelApi.class})
public class RpcConfiguration {
}
