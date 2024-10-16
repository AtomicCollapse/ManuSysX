package top.hondaman.cloud.infra.system.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 鉴权实体，登陆成功后缓存到redis中，通过TokenUserHandlerMethodArgumentResolver传到Controller入参
 */
@Getter
@Setter
public class UserInfoToken {
    private String accessToken;
    private String userId;
    private String userName;
    private Integer userType;
}
