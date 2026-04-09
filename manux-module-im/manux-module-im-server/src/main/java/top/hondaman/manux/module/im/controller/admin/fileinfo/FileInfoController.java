package top.hondaman.manux.module.im.controller.admin.fileinfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.hondaman.manux.framework.common.pojo.CommonResult;
import top.hondaman.manux.module.im.controller.admin.fileinfo.vo.UploadImageVO;
import top.hondaman.manux.module.im.service.fileinfo.FileInfoService;

import static top.hondaman.manux.framework.common.pojo.CommonResult.success;


@Tag(name = "管理后台 - IM文件")
@RestController
@RequestMapping("/im/file")
@Validated
public class FileInfoController {

    @Resource
    private FileInfoService fileInfoService;


    @PostMapping("/image/upload")
    @Operation(summary = "上传图片", description = "上传图片,上传后返回原图和缩略图的url")
    public CommonResult<UploadImageVO> uploadImage(@RequestParam("file") MultipartFile file,
                                                   @RequestParam(defaultValue = "true") Boolean isPermanent, @RequestParam(defaultValue = "50") Long thumbSize) {
        return success(fileInfoService.uploadImage(file, isPermanent,thumbSize));
    }

    @PostMapping("/file/upload")
    @Operation(summary = "上传文件", description = "上传文件，上传后返回文件url")
    public CommonResult<String> uploadFile(@RequestParam("file") MultipartFile file) {
        return success(fileInfoService.uploadFile(file));
    }
}