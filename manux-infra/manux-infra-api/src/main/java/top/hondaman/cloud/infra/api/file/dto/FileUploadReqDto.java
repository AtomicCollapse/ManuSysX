package top.hondaman.cloud.infra.api.file.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class FileUploadReqDto {
    //源文件名称
    private String name;
    //文件路径
    private String path;
    //文件内容
    @NotEmpty(message = "文件内容不能为空")
    private byte[] content;
}