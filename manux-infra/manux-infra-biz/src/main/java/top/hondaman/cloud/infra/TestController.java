package top.hondaman.cloud.infra;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hondaman.cloud.framework.common.pojo.CommonResult;
import top.hondaman.cloud.infra.enums.ApiConstants;
import top.hondaman.cloud.framework.asyncimport.core.AsyncImportMasterHandler;

import javax.annotation.Resource;

@RestController
@RequestMapping(ApiConstants.PREFIX + "/test")
public class TestController {
    @Resource
    private AsyncImportMasterHandler asyncImportMasterHandler;

    @GetMapping("")
    public String getMessage(){
        return "请求成功";
    }

    @GetMapping("masterHandlerTest")
    public CommonResult handleTest(){
        return CommonResult.success(asyncImportMasterHandler.testGetHandlerList());
    }
}
