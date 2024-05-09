package com.ganhua.cloud.pms.api.comp;
import com.ganhua.cloud.framework.common.pojo.CommonResult;
import com.ganhua.cloud.pms.api.comp.dto.CompDTO;
import com.ganhua.cloud.pms.enums.ApiConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient
public interface CompApi {

    String PREFIX = ApiConstants.PREFIX + "/comp";

    @GetMapping(PREFIX + "/get")
    CommonResult<CompDTO> getComp(@RequestParam("sid") String sid);
}
