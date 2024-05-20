package top.hondaman.cloud.infra.api.file;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import top.hondaman.cloud.framework.common.pojo.CommonResult;
import top.hondaman.cloud.infra.api.file.controller.FileApi;
import top.hondaman.cloud.infra.api.file.dto.FileUploadReqDto;
import top.hondaman.cloud.infra.service.file.FileService;

import javax.annotation.Resource;
import javax.validation.Valid;

import static top.hondaman.cloud.framework.common.pojo.CommonResult.success;

@RestController
@Validated
public class FileApiImpl implements FileApi {
    @Resource
    private FileService fileService;

    @Override
    public CommonResult<String> uploadFile(@Valid FileUploadReqDto fileUploadReqDto) {
        return success(fileService.uploadFile(fileUploadReqDto.getName(),fileUploadReqDto.getPath(),fileUploadReqDto.getContent()));
    }
}
