package top.hondaman.cloud.infra.file.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import top.hondaman.cloud.framework.common.pojo.CommonResult;
import top.hondaman.cloud.infra.file.dto.FileUploadParam;
import top.hondaman.cloud.infra.file.service.FileService;

import javax.annotation.Resource;
import javax.validation.Valid;

import static top.hondaman.cloud.framework.common.pojo.CommonResult.success;

@RestController
@Validated
public class FileApiImpl implements FileApi {
    @Resource
    private FileService fileService;

    @Override
    public CommonResult<String> uploadFile(@Valid FileUploadParam fileUploadParam) {
        return success(fileService.uploadFile(fileUploadParam.getName(), fileUploadParam.getPath(), fileUploadParam.getContent()));
    }
}
