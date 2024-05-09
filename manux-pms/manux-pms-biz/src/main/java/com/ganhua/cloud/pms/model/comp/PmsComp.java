package com.ganhua.cloud.pms.model.comp;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ganhua.cloud.framework.common.pojo.BasicModel;
import lombok.Data;

@Data
@TableName("t_pms_comp")
public class PmsComp extends BasicModel {
    private String name;
    private String code;
}
