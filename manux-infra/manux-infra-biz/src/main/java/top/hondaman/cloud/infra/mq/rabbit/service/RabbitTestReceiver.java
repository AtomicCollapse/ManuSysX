package top.hondaman.cloud.infra.mq.rabbit.service;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.hondaman.cloud.infra.system.model.UserInfo;

@Component
@RabbitListener(queues = "importTask")
public class RabbitTestReceiver {

    @RabbitHandler
    public void process(UserInfo userInfo){
        System.out.println(userInfo);
    }
}
