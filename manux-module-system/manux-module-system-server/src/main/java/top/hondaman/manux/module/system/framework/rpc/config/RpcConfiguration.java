package top.hondaman.manux.module.system.framework.rpc.config;

import top.hondaman.manux.module.infra.api.config.ConfigApi;
import top.hondaman.manux.module.infra.api.file.FileApi;
import top.hondaman.manux.module.infra.api.websocket.WebSocketSenderApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "systemRpcConfiguration", proxyBeanMethods = false)
@EnableFeignClients(clients = {FileApi.class, WebSocketSenderApi.class, ConfigApi.class})
public class RpcConfiguration {
}
