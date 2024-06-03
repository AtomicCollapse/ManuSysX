package top.hondaman.cloud.framework.redis.oauth2.service;

import org.springframework.stereotype.Service;
import top.hondaman.cloud.framework.redis.oauth2.dto.OAuth2AccessTokenDto;
import top.hondaman.cloud.framework.redis.oauth2.mapper.OAuth2AccessTokenRedisDAO;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OAuth2TokenServiceImpl implements OAuth2TokenService{
    @Resource
    private OAuth2AccessTokenRedisDAO oAuth2AccessTokenRedisDAO;

    @Override
    public OAuth2AccessTokenDto createAccessToken(String userId,Integer userType) {
        OAuth2AccessTokenDto accessTokenDto = new OAuth2AccessTokenDto();
        accessTokenDto.setAccessToken(UUID.randomUUID().toString());
        accessTokenDto.setUserId(userId);
        accessTokenDto.setUserType(userType);
        accessTokenDto.setExpiresTime(LocalDateTime.now().plusHours(12));

        oAuth2AccessTokenRedisDAO.set(accessTokenDto);
        return accessTokenDto;
    }

    @Override
    public OAuth2AccessTokenDto removeAccessToken(String accessToken) {
        OAuth2AccessTokenDto accessTokenDto = oAuth2AccessTokenRedisDAO.get(accessToken);
        if (accessTokenDto == null) {
            return null;
        }
        oAuth2AccessTokenRedisDAO.delete(accessToken);
        return accessTokenDto;
    }

    @Override
    public OAuth2AccessTokenDto getUserInfo(String accessToken){
        return oAuth2AccessTokenRedisDAO.get(accessToken);
    }
}
