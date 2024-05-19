package top.hondaman.cloud.gateway.filter;

import cn.hutool.core.util.StrUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.hondaman.cloud.framework.redis.oauth2.dto.OAuth2AccessTokenDto;
import top.hondaman.cloud.framework.redis.oauth2.mapper.OAuth2AccessTokenRedisDAO;

import javax.annotation.Resource;
import java.util.UUID;

@Order(1)
@Component
public class TokenAuthorizationFilter implements GlobalFilter {
    @Resource
    private OAuth2AccessTokenRedisDAO oAuth2AccessTokenRedisDAO;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        //获取请求目标url
        String path = request.getURI().getPath();
        if(StrUtil.isNotEmpty(path) && path.equals("/api/pms/userInfo/login")){
            //特殊请求，直接放行
            return chain.filter(exchange.mutate().request(request).build());
        }else{
            String authorization = request.getHeaders().getFirst("Authorization");
            if(StrUtil.isEmpty(authorization)){
                //拦截
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String accessToken = authorization.replace("Bearer ","");
            //从Redis校验token
            OAuth2AccessTokenDto oAuth2AccessTokenDto = oAuth2AccessTokenRedisDAO.get(accessToken);
            if(oAuth2AccessTokenDto != null){
                //放行
                return chain.filter(exchange.mutate().request(request).build());
            }
        }


        //拦截
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
