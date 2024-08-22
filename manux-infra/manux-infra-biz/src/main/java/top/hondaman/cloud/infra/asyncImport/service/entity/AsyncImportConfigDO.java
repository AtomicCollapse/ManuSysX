package top.hondaman.cloud.infra.asyncImport.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;
import top.hondaman.cloud.framework.common.pojo.BasicModel;

@Data
@TableName("t_infra_import_config")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AsyncImportConfigDO extends BasicModel {
    private String systemCode;
    private String taskCode;
}
