package top.hondaman.cloud.infra.asyncImport.handler;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.hondaman.cloud.infra.asyncImport.mapper.ImportConfigMapper;
import top.hondaman.cloud.infra.asyncImport.model.ImportTask;
import top.hondaman.cloud.infra.mq.enums.QueueConstants;

import javax.annotation.Resource;

/**
 * 导入任务消费者
 */
@Component
@RabbitListener(queues = QueueConstants.IMPORT)
public class AsyncImportHandler {
    @Resource
    private ImportConfigMapper importConfigMapper;

    @RabbitHandler
    public void execute(ImportTask task){
        /**
         * 从队列中消费到导入任务后，根据任务的`systemCode`+`taskCode`查询[导入配置表]，解析到对应的业务处理类，将任务分发下去
         */
        // TODO 该实现需要修改

    }
}
