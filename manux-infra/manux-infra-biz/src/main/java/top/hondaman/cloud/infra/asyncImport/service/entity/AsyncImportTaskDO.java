package top.hondaman.cloud.infra.asyncImport.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;
import top.hondaman.cloud.framework.common.pojo.BasicModel;

@Data
@TableName("t_infra_import_task")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AsyncImportTaskDO extends BasicModel {
    private String systemCode;
    private String taskCode;
    private String status;
}
