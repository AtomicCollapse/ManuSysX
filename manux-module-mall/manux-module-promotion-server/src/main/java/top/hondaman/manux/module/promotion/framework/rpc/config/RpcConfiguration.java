package top.hondaman.manux.module.promotion.framework.rpc.config;

import top.hondaman.manux.module.infra.api.websocket.WebSocketSenderApi;
import top.hondaman.manux.module.member.api.user.MemberUserApi;
import top.hondaman.manux.module.product.api.category.ProductCategoryApi;
import top.hondaman.manux.module.product.api.sku.ProductSkuApi;
import top.hondaman.manux.module.product.api.spu.ProductSpuApi;
import top.hondaman.manux.module.system.api.social.SocialClientApi;
import top.hondaman.manux.module.system.api.user.AdminUserApi;
import top.hondaman.manux.module.trade.api.order.TradeOrderApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "promotionRpcConfiguration", proxyBeanMethods = false)
@EnableFeignClients(clients = {ProductSkuApi.class, ProductSpuApi.class, ProductCategoryApi.class,
        MemberUserApi.class, TradeOrderApi.class, AdminUserApi.class, SocialClientApi.class,
        WebSocketSenderApi.class})
public class RpcConfiguration {
}
