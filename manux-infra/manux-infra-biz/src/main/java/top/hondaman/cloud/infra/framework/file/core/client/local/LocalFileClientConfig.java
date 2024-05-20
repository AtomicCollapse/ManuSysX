package top.hondaman.cloud.infra.framework.file.core.client.local;

import lombok.Data;
import top.hondaman.cloud.infra.framework.file.core.client.FileClientConfig;

import javax.validation.constraints.NotEmpty;

/**
 * 本地文件客户端的配置类
 *
 * @author 芋道源码
 */
@Data
public class LocalFileClientConfig implements FileClientConfig {

    /**
     * 基础路径
     */
    @NotEmpty(message = "基础路径不能为空")
    private String basePath;

    /**
     * 自定义域名
     */
    @NotEmpty(message = "domain 不能为空")
    private String domain;

}
