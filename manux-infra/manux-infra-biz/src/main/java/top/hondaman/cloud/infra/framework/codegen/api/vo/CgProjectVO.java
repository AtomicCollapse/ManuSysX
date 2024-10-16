package top.hondaman.cloud.infra.framework.codegen.api.vo;

import lombok.Data;
import top.hondaman.cloud.framework.common.pojo.BasicModel;

@Data
public class CgProjectVO extends BasicModel {
    private String name;
    private String packageName;
}
