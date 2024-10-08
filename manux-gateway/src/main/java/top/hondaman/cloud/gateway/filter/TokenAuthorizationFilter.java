package top.hondaman.cloud.gateway.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.hondaman.cloud.framework.common.pojo.CommonResult;
import top.hondaman.cloud.framework.redis.oauth2.dto.OAuth2AccessTokenDto;
import top.hondaman.cloud.framework.redis.oauth2.mapper.OAuth2AccessTokenRedisDAO;

import javax.annotation.Resource;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static top.hondaman.cloud.framework.common.exception.enums.GlobalErrorCodeConstants.UNAUTHORIZED;

@Order(1)
@Component
public class TokenAuthorizationFilter implements GlobalFilter {
    @Resource
    private OAuth2AccessTokenRedisDAO oAuth2AccessTokenRedisDAO;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //需要放行的请求路径
        List<String> passPath = new ArrayList<>(Arrays.asList("/api/pms/userInfo/login","/api/pms/userInfo/insert"));

        ServerHttpRequest request = exchange.getRequest();
        //获取请求目标url
        String path = request.getURI().getPath();
        if(StrUtil.isNotEmpty(path) && passPath.contains(path)){
            //特殊请求，直接放行
            return chain.filter(exchange.mutate().request(request).build());
        }else{
            String authorization = request.getHeaders().getFirst("Authorization");
            if(StrUtil.isEmpty(authorization)){
                //拦截
                return unAuthorized(exchange);
            }

            String accessToken = authorization.replace("Bearer ","");
            //从Redis校验token
            OAuth2AccessTokenDto oAuth2AccessTokenDto = oAuth2AccessTokenRedisDAO.get(accessToken);
            if(oAuth2AccessTokenDto != null){
//                //没找到好的办法，先直接把 token 塞到请求头里去，交给业务系统自己去redis拿用户信息
//                request = request.mutate().header("accessToken",oAuth2AccessTokenDto.getAccessToken()).build();
                /**
                 * 此处修改
                 * 交给自定义servlet参数解析器[TokenUserHandlerMethodArgumentResolver]处理
                 */
                //放行
                return chain.filter(exchange.mutate().request(request).build());
            }
        }


        //拦截
        return unAuthorized(exchange);
    }

    /**
     * 返回自定义响应
     */
    private Mono<Void> unAuthorized(ServerWebExchange exchange){
        ServerHttpResponse response = exchange.getResponse();


        CommonResult result = CommonResult.error(UNAUTHORIZED);
        DataBuffer buffer = response.bufferFactory().wrap(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type","application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }
}
