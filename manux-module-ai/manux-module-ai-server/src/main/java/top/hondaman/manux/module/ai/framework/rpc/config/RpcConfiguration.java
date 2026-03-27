package top.hondaman.manux.module.ai.framework.rpc.config;

import top.hondaman.manux.module.infra.api.file.FileApi;
import top.hondaman.manux.module.system.api.user.AdminUserApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "aiRpcConfiguration", proxyBeanMethods = false)
@EnableFeignClients(clients = {FileApi.class, AdminUserApi.class})
public class RpcConfiguration {
}
