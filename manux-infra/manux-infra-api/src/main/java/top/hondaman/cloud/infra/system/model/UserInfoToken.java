package top.hondaman.cloud.infra.system.model;

import lombok.Getter;

@Getter
public class UserInfoToken {
    private String accessToken;
    private String userId;
    private String userName;
    private String userType;
}
