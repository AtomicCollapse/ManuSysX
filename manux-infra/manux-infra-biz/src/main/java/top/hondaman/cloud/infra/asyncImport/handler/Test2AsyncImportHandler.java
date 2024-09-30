package top.hondaman.cloud.infra.asyncImport.handler;

import org.springframework.stereotype.Component;
import top.hondaman.cloud.system.asyncimport.core.AsyncImportHandler;

@Component
public class Test2AsyncImportHandler implements AsyncImportHandler {
    @Override
    public String getSystemCode() {
        return null;
    }

    @Override
    public String getTaskCode() {
        return null;
    }

    @Override
    public void checkData() {

    }

    @Override
    public void persistData() {

    }
}
