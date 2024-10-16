package top.hondaman.cloud.infra.framework.codegen.api.dto;

import lombok.Data;
import top.hondaman.cloud.framework.common.pojo.BasicParam;

@Data
public class CgProjectDTO extends BasicParam {
    private String name;
    private String packageName;
}
