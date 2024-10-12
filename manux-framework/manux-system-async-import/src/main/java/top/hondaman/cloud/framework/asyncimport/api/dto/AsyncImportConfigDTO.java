package top.hondaman.cloud.framework.asyncimport.api.dto;

import lombok.Data;
import top.hondaman.cloud.framework.common.pojo.BasicModel;

import javax.validation.constraints.NotEmpty;

@Data
public class AsyncImportConfigDTO extends BasicModel {
    @NotEmpty(message = "系统编码不能为空")
    private String systemCode;
    @NotEmpty(message = "任务编码不能为空")
    private String taskCode;
}
