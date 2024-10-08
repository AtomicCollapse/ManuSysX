package top.hondaman.cloud.infra.file.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import top.hondaman.cloud.framework.common.pojo.CommonResult;
import top.hondaman.cloud.infra.file.dto.FileUploadDTO;
import top.hondaman.cloud.infra.enums.ApiConstants;

import javax.validation.Valid;

@FeignClient(name = ApiConstants.NAME,contextId = "FileApi")
public interface FileApi {
    String PREFIX = ApiConstants.PREFIX + "/file";

    /**
     * 保存文件，并返回文件的访问路径
     *
     * @param content 文件内容
     * @return 文件路径
     */
    default String uploadFile(byte[] content){
        return uploadFile(null, null, content);
    }

    /**
     * 保存文件，并返回文件的访问路径
     *
     * @param path 文件路径
     * @param content 文件内容
     * @return 文件路径
     */
    default String uploadFile(String path, byte[] content) {
        return uploadFile(null, path, content);
    }


    /**
     * 保存文件，并返回文件的访问路径
     *
     * @param name 原文件名称
     * @param path 文件路径
     * @param content 文件内容
     * @return 文件路径
     */
    default String uploadFile(@RequestParam("name") String name,
                              @RequestParam("path") String path,
                              @RequestParam("content") byte[] content) {
        return uploadFile(new FileUploadDTO().setPath(path).setName(name).setContent(content)).getData();
    }

    @PostMapping(PREFIX + "/upload")
    CommonResult<String> uploadFile(@Valid @RequestBody FileUploadDTO fileUploadDTO);

}
