package top.hondaman.cloud.infra.framework.codegen.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import top.hondaman.cloud.framework.common.pojo.BasicModel;

@Data
@TableName("t_infra_cg_module")
public class CgModuleDO extends BasicModel {
    private String name;
    private String packageName;
    private String projectId;
}
