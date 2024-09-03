package top.hondaman.cloud.infra.file.api;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import top.hondaman.cloud.framework.common.pojo.CommonResult;
import top.hondaman.cloud.infra.file.dto.FileUploadDTO;
import top.hondaman.cloud.infra.file.service.FileService;

import javax.annotation.Resource;
import javax.validation.Valid;

import static top.hondaman.cloud.framework.common.pojo.CommonResult.success;

@RestController
@Validated
public class FileController implements FileApi {
    @Resource
    private FileService fileService;

    @Override
    public CommonResult<String> uploadFile(@Valid FileUploadDTO fileUploadDTO) {
        return success(fileService.uploadFile(fileUploadDTO.getName(), fileUploadDTO.getPath(), fileUploadDTO.getContent()));
    }
}
