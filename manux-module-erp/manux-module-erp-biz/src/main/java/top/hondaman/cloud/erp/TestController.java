package top.hondaman.cloud.erp;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hondaman.cloud.erp.enums.ApiConstants;
import top.hondaman.cloud.framework.common.pojo.CommonResult;
import top.hondaman.cloud.infra.asyncImport.api.AsyncImportTaskApi;
import top.hondaman.cloud.infra.file.api.FileApi;
import top.hondaman.cloud.infra.system.model.UserInfoToken;

import javax.annotation.Resource;

@RestController
@RequestMapping(ApiConstants.PREFIX + "/test")
public class TestController {
    @Resource
    private AsyncImportTaskApi asyncImportTaskApi;
    @Resource
    private FileApi fileApi;

    @PostMapping("")
    public CommonResult testLastTaskApi(){
        return asyncImportTaskApi.getLastTask("test1","test2");
    }

    @GetMapping("uploadFile")
    public String uploadFile(){

        fileApi.uploadFile(FileUtil.readBytes("C:\\Users\\Cheung\\Desktop\\-\\壁纸\\壁纸\\2.png"));

        return "请求成功到达";
    }

    @GetMapping("")
    public String testReq(UserInfoToken userInfoToken){
        System.out.println(JSONUtil.toJsonStr(userInfoToken));
        return "请求成功到达aaaa";
    }
}
