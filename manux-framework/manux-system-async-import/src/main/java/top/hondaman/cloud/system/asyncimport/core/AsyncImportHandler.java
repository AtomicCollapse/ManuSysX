package top.hondaman.cloud.system.asyncimport.core;

/**
 * 异步导入消费者抽象接口
 */
public interface AsyncImportHandler {

    /**
     * 系统编码
     * @return
     */
    String getSystemCode();

    /**
     * 任务编码
     * @return
     */
    String getTaskCode();

    /**
     * 业务校验方法
     */
    void checkData();

    /**
     * 数据持久化方法
     */
    void persistData();
}
