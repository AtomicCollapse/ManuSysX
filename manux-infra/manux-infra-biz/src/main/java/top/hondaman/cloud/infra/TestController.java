package top.hondaman.cloud.infra;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/infra/test")
public class TestController {

    @GetMapping("")
    public String test(){
        return "请求成功到达";
    }
}
