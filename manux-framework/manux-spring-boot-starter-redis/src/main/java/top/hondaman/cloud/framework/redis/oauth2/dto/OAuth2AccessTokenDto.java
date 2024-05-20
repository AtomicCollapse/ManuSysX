package top.hondaman.cloud.framework.redis.oauth2.dto;

import lombok.Data;
//import top.hondaman.cloud.infra.system.enums.UserTypeEnum;
import java.time.LocalDateTime;


@Data
public class OAuth2AccessTokenDto {
    /**
     * access token
     */
    private String accessToken;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户类型
     *

     */
    private Integer userType;
    /**
     * 过期时间
     */
    private LocalDateTime expiresTime;
}
