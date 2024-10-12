package top.hondaman.cloud.framework.asyncimport.api.vo;

import lombok.Data;
import top.hondaman.cloud.framework.common.pojo.BasicModel;

@Data
public class AsyncImportTaskVO extends BasicModel {
    //系统编码
    private String systemCode;
    //任务编码
    private String taskCode;
    //任务状态
    private String status;
    //原始文件在文件服务器上的相对路径
    private String originFilePath;
    //校验通过数据文件在文件服务器上的相对路径
    private String correctFilePath;
    //校验失败数据文件在文件服务器上的相对路径
    private String wrongFilePath;
    //导入异常原因
    private String errorMessage;
}
