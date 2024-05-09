package com.ganhua.cloud.pms.service.comp;

import com.ganhua.cloud.framework.common.pojo.PageResult;
import com.ganhua.cloud.pms.dto.comp.PmsCompDO;
import com.ganhua.cloud.pms.dto.comp.PmsCompVO;

public interface PmsCompService {
    PageResult<PmsCompDO> getList(PmsCompVO pmsCompVO);
}
