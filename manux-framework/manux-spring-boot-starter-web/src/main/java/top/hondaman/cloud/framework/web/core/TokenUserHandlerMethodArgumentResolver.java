package top.hondaman.cloud.framework.web.core;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import top.hondaman.cloud.framework.common.core.SpringContextHolder;
import top.hondaman.cloud.framework.redis.oauth2.dto.OAuth2AccessTokenDto;
import top.hondaman.cloud.framework.redis.oauth2.mapper.OAuth2AccessTokenRedisDAO;
import top.hondaman.cloud.infra.system.model.UserInfoToken;

import javax.annotation.Resource;

/**
 * 处理Token信息
 * 把token串从redis中转成UserInfo传给controller
 */
public class TokenUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final OAuth2AccessTokenRedisDAO oAuth2AccessTokenRedisDAO;
    public TokenUserHandlerMethodArgumentResolver () {
        this.oAuth2AccessTokenRedisDAO = SpringContextHolder.getBean(OAuth2AccessTokenRedisDAO.class);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserInfoToken.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        authorization = authorization.replace("Bearer ","");
        OAuth2AccessTokenDto oAuth2AccessTokenDto = oAuth2AccessTokenRedisDAO.get(authorization);
        UserInfoToken res = new UserInfoToken();
        res.setUserId(oAuth2AccessTokenDto.getUserId());
        res.setUserName(oAuth2AccessTokenDto.getUserName());
        res.setUserType(oAuth2AccessTokenDto.getUserType());
        res.setAccessToken(oAuth2AccessTokenDto.getAccessToken());
        return res;
    }
}
