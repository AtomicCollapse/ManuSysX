package top.hondaman.cloud.infra;

import cn.hutool.core.io.FileUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hondaman.cloud.infra.api.file.controller.FileApi;

import javax.annotation.Resource;

@RestController
@RequestMapping("/infra/test")
public class TestController {
    @Resource
    private FileApi fileApi;

    @GetMapping("")
    public String test(){

        fileApi.uploadFile(FileUtil.readBytes("C:\\Users\\Cheung\\Desktop\\-\\壁纸\\壁纸\\2.jpg"));

        return "请求成功到达";
    }
}
