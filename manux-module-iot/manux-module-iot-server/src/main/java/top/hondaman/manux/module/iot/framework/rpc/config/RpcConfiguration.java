package top.hondaman.manux.module.iot.framework.rpc.config;

import top.hondaman.manux.module.system.api.mail.MailSendApi;
import top.hondaman.manux.module.system.api.notify.NotifyMessageSendApi;
import top.hondaman.manux.module.system.api.sms.SmsSendApi;
import top.hondaman.manux.module.system.api.user.AdminUserApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "iotRpcConfiguration", proxyBeanMethods = false)
@EnableFeignClients(clients = {
        AdminUserApi.class, SmsSendApi.class, MailSendApi.class, NotifyMessageSendApi.class
})
public class RpcConfiguration {
}
