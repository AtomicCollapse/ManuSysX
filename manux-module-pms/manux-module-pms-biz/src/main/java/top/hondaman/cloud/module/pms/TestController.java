package top.hondaman.cloud.module.pms;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hondaman.cloud.framework.common.pojo.CommonResult;

@RestController
@RequestMapping("pms/test")
public class TestController {

    @GetMapping("")
    public String testReq(){
        return "请求成功到达";
    }
}
