package top.hondaman.manux.module.im.framework.rpc.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import top.hondaman.manux.module.infra.api.file.FileApi;

@Configuration(value = "imRpcConfiguration", proxyBeanMethods = false)
@EnableFeignClients(clients = FileApi.class)
public class RpcConfiguration {
}
