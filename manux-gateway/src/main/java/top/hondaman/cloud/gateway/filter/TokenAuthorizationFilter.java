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

import java.util.UUID;

@Order(1)
@Component
public class TokenAuthorizationFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String auth = request.getHeaders().getFirst("Authorization");
        if(StrUtil.isNotBlank(auth) && auth.replace("Bearer ","").equals("admin")){
            request = request.mutate().header("userId",UUID.randomUUID().toString()).build();
            //放行
            return chain.filter(exchange.mutate().request(request).build());
        }

        //拦截
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
