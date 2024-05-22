package top.hondaman.cloud.infra.framework.rpc.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import top.hondaman.cloud.infra.api.file.controller.FileApi;

@Configuration(proxyBeanMethods = false)
@EnableFeignClients(clients = FileApi.class)
public class RpcConfiguration {
}
