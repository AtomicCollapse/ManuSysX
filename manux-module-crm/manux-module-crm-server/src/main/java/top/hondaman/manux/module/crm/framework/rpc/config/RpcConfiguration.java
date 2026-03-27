package top.hondaman.manux.module.crm.framework.rpc.config;

import top.hondaman.manux.module.bpm.api.task.BpmProcessInstanceApi;
import top.hondaman.manux.module.system.api.dept.DeptApi;
import top.hondaman.manux.module.system.api.dept.PostApi;
import top.hondaman.manux.module.system.api.logger.OperateLogApi;
import top.hondaman.manux.module.system.api.user.AdminUserApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "crmRpcConfiguration", proxyBeanMethods = false)
@EnableFeignClients(clients = {AdminUserApi.class, DeptApi.class, PostApi.class,
        OperateLogApi.class,
        BpmProcessInstanceApi.class})
public class RpcConfiguration {
}
