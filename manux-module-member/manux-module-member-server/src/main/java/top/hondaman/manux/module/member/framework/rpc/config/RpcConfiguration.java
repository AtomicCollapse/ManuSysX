package top.hondaman.manux.module.member.framework.rpc.config;

import top.hondaman.manux.module.system.api.logger.LoginLogApi;
import top.hondaman.manux.module.system.api.sms.SmsCodeApi;
import top.hondaman.manux.module.system.api.social.SocialClientApi;
import top.hondaman.manux.module.system.api.social.SocialUserApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "memberRpcConfiguration", proxyBeanMethods = false)
@EnableFeignClients(clients = {SmsCodeApi.class, LoginLogApi.class, SocialUserApi.class, SocialClientApi.class})
public class RpcConfiguration {
}
