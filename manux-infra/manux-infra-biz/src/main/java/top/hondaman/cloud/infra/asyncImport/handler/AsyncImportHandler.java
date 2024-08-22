package top.hondaman.cloud.infra.asyncImport.handler;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.hondaman.cloud.infra.asyncImport.controller.vo.AsyncImportTaskVO;
import top.hondaman.cloud.infra.asyncImport.mapper.AsyncImportConfigMapper;
import top.hondaman.cloud.infra.asyncImport.model.ImportTask;
import top.hondaman.cloud.infra.asyncImport.service.entity.AsyncImportTaskDO;
import top.hondaman.cloud.infra.mq.enums.QueueConstants;

import javax.annotation.Resource;

/**
 * 导入任务消费者
 */
@Component
@RabbitListener(queues = QueueConstants.IMPORT)
public class AsyncImportHandler {
    @Resource
    private AsyncImportConfigMapper importConfigMapper;

    @RabbitHandler
    public void execute(AsyncImportTaskDO task){
        /**
         * 从队列中消费到导入任务后，根据任务的`systemCode`+`taskCode`查询[导入配置表]，解析到对应的业务处理类，将任务分发下去
         */
        // TODO 该实现需要修改
        System.out.println(task);
    }
}
