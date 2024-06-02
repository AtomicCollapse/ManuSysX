package top.hondaman.cloud.infra.asyncImport.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import top.hondaman.cloud.framework.common.pojo.BasicModel;

@Data
@TableName("t_infra_import_config")
public class ImportConfig extends BasicModel {
    private String systemCode;
    private String taskCode;
}
