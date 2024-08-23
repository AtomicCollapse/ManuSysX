package top.hondaman.cloud.infra.asyncImport.handler;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.hondaman.cloud.infra.asyncImport.mapper.AsyncImportConfigMapper;
import top.hondaman.cloud.infra.asyncImport.model.ImportTask;
import top.hondaman.cloud.infra.asyncImport.service.AsyncImportHandler;
import top.hondaman.cloud.infra.asyncImport.service.entity.AsyncImportTaskDO;

import javax.annotation.Resource;

/**
 * 导入任务消费者
 */
@Component
public class TestAsyncImportHandler implements AsyncImportHandler {

    @Override
    public String getSystemCode() {
        return "test1";
    }

    @Override
    public String getTaskCode() {
        return "test2";
    }

    @Override
    public void checkData() {
        System.out.println("开始校验");
        System.out.println("校验结束");
    }

    @Override
    public void persistData() {
        System.out.println("开始导入");
        System.out.println("导入结束");
    }
}
