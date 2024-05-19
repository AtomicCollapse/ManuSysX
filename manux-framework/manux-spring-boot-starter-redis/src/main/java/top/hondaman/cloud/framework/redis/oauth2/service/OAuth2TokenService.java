package top.hondaman.cloud.framework.redis.oauth2.service;


import top.hondaman.cloud.framework.redis.oauth2.dto.OAuth2AccessTokenDto;

/**
 * OAuth2.0 Token Service 接口
 *
 * 从功能上，和 Spring Security OAuth 的 DefaultTokenServices + JdbcTokenStore 的功能，提供访问令牌、刷新令牌的操作
 *
 */
public interface OAuth2TokenService {
    /**
     * 创建访问令牌
     * @param userId 用户id
     * @param userType 用户类型
     * @return 访问令牌的信息
     */
    OAuth2AccessTokenDto createAccessToken(String userId,Integer userType);

    /**
     * 移除访问令牌
     * @param accessToken
     * @return 访问令牌的信息
     */
    OAuth2AccessTokenDto removeAccessToken(String accessToken);
}
