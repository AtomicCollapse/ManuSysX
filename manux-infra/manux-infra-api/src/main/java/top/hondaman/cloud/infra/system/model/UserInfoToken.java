package top.hondaman.cloud.infra.system.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoToken {
    private String accessToken;
    private String userId;
    private String userName;
    private Integer userType;
}
