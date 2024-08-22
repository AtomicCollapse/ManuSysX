package top.hondaman.cloud.infra.asyncImport.controller.dto;

import lombok.Data;
import top.hondaman.cloud.framework.common.pojo.BasicModel;

import javax.validation.constraints.NotEmpty;

@Data
public class AsyncImportTaskDTO extends BasicModel {
    //系统编码
    @NotEmpty(message = "系统编码不能为空")
    private String systemCode;
    //任务编码
    @NotEmpty(message = "任务编码不能为空")
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