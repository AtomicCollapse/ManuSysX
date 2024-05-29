package top.hondaman.cloud.infra.mq.rabbit.controller;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hondaman.cloud.framework.common.pojo.CommonResult;
import top.hondaman.cloud.infra.system.model.UserInfo;

import javax.annotation.Resource;

@RestController
@RequestMapping("rabbit/test")
public class RabbitTestController {
    @Resource
    private AmqpTemplate amqpTemplate;

    @GetMapping("")
    public CommonResult sendMessage(){
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName("张三");
        amqpTemplate.convertAndSend("importTask",userInfo);

        return CommonResult.success("成功");
    }
}
