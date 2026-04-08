package top.hondaman.manux.module.im.framework.im.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "anplus.im.file-properties")
public class IMFileProperties {
    private String imagePath;

    private String filePath;

    private String videoPath;

    private Integer expireIn;
}
