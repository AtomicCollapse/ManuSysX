package com.ganhua.cloud.pms.dto.comp;

import com.ganhua.cloud.framework.common.pojo.PageParam;
import lombok.Data;

@Data
public class PmsCompVO extends PageParam {
    private String sid;
    private String name;
    private String code;
}
