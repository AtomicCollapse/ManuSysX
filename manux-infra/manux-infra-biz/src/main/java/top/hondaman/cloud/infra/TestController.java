package top.hondaman.cloud.infra;

import cn.hutool.core.io.FileUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hondaman.cloud.infra.enums.ApiConstants;

import javax.annotation.Resource;

@RestController
@RequestMapping(ApiConstants.PREFIX + "/test")
public class TestController {

    @GetMapping("")
    public String getMessage(){
        return "请求成功";
    }
}
