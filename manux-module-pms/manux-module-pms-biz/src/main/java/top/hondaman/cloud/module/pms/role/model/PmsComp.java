package top.hondaman.cloud.module.pms.role.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import top.hondaman.cloud.framework.common.pojo.BasicModel;

@Getter
@TableName("t_pms_comp")
public class PmsComp extends BasicModel {
    private String name;
    private String code;
}
