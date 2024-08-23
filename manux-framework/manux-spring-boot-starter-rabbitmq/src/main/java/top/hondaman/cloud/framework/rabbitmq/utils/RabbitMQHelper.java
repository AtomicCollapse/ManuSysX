package top.hondaman.cloud.framework.rabbitmq.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import top.hondaman.cloud.framework.common.core.SpringContextHolder;

@Slf4j
public class RabbitMQHelper {
    private static final AmqpAdmin amqpAdmin = SpringContextHolder.getBean(AmqpAdmin.class);
    private static final ConnectionFactory connectionFactory = SpringContextHolder.getBean(ConnectionFactory.class);
    private static final RabbitTemplate rabbitTemplate = SpringContextHolder.getBean(RabbitTemplate.class);

    public static void createQueue(String queueName) {
        Queue queue = new Queue(queueName, true);
        if(isExistsQueue(queueName)){
            amqpAdmin.declareQueue(queue);
        }else{
            log.debug(String.format("%s队列已存在。跳过",queueName));
        }
    }

    public static void deleteQueue(String queueName) {
        amqpAdmin.deleteQueue(queueName);
    }

    public static void subscribeQueue(String queueName, Object messageListener, String methodName) {
        log.debug("subscribeQueue: {}", queueName);
        // 创建一个消息监听适配器
        MessageListenerAdapter listenerAdapter = new MessageListenerAdapter(messageListener, methodName);
        // 创建 SimpleMessageListenerContainer
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        // 启动容器
        container.start();
    }

    public static void sendMessage(String queueName, Object message) {
        if (!isExistsQueue(queueName)){
            throw new RuntimeException(String.format("[%s]队列不存在",queueName));
        }
        log.debug("sendMessage, queueName: {} , message: {}", queueName, message);
        rabbitTemplate.convertAndSend(queueName, message);
    }

    public static boolean isExistsQueue(String queueName){
        return amqpAdmin.getQueueInfo(queueName) == null ? false : true;
    }
}
