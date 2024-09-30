package top.hondaman.cloud.infra.asyncImport.handler;

import org.springframework.stereotype.Component;
import top.hondaman.cloud.infra.asyncImport.core.AbstractAsyncImportHandler;
import top.hondaman.cloud.system.asyncimport.core.AsyncImportHandler;


/**
 * 导入任务消费者
 */
@Component
public class Test1AsyncImportHandler implements AsyncImportHandler {

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
