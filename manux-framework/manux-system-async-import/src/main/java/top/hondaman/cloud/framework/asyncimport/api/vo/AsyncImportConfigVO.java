package top.hondaman.cloud.framework.asyncimport.api.vo;

import lombok.Data;
import top.hondaman.cloud.framework.common.pojo.BasicModel;

@Data
public class AsyncImportConfigVO extends BasicModel {
    private String systemCode;
    private String taskCode;
}
