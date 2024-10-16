package top.hondaman.cloud.infra.framework.codegen.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import top.hondaman.cloud.framework.common.pojo.BasicModel;

@Data
@TableName("t_infra_cg_data_source")
public class CgDataSourceDO extends BasicModel {
    private String name;
    private String type;
    private String ipAddr;
    private Integer port;
}
