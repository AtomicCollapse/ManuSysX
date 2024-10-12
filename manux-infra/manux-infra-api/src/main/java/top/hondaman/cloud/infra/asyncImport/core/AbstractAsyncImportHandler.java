package top.hondaman.cloud.infra.asyncImport.core;

import top.hondaman.cloud.framework.asyncimport.core.AsyncImportHandler;

/**
 * 抽象异步导入处理器
 * 对 AsyncImportHandler 接口做了增强，实现了应该由异步导入启动类负责的更新任务状态逻辑
 */
public abstract class AbstractAsyncImportHandler implements AsyncImportHandler {

    public void afterCheckData(){

    }

    public void afterPersistData(){

    }
}
