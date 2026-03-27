package top.hondaman.manux.module.trade.framework.rpc.config;

import top.hondaman.manux.module.member.api.address.MemberAddressApi;
import top.hondaman.manux.module.member.api.config.MemberConfigApi;
import top.hondaman.manux.module.member.api.level.MemberLevelApi;
import top.hondaman.manux.module.member.api.point.MemberPointApi;
import top.hondaman.manux.module.member.api.user.MemberUserApi;
import top.hondaman.manux.module.pay.api.order.PayOrderApi;
import top.hondaman.manux.module.pay.api.refund.PayRefundApi;
import top.hondaman.manux.module.pay.api.transfer.PayTransferApi;
import top.hondaman.manux.module.pay.api.wallet.PayWalletApi;
import top.hondaman.manux.module.product.api.category.ProductCategoryApi;
import top.hondaman.manux.module.product.api.comment.ProductCommentApi;
import top.hondaman.manux.module.product.api.sku.ProductSkuApi;
import top.hondaman.manux.module.product.api.spu.ProductSpuApi;
import top.hondaman.manux.module.promotion.api.bargain.BargainActivityApi;
import top.hondaman.manux.module.promotion.api.bargain.BargainRecordApi;
import top.hondaman.manux.module.promotion.api.combination.CombinationRecordApi;
import top.hondaman.manux.module.promotion.api.coupon.CouponApi;
import top.hondaman.manux.module.promotion.api.discount.DiscountActivityApi;
import top.hondaman.manux.module.promotion.api.point.PointActivityApi;
import top.hondaman.manux.module.promotion.api.reward.RewardActivityApi;
import top.hondaman.manux.module.promotion.api.seckill.SeckillActivityApi;
import top.hondaman.manux.module.system.api.notify.NotifyMessageSendApi;
import top.hondaman.manux.module.system.api.social.SocialClientApi;
import top.hondaman.manux.module.system.api.social.SocialUserApi;
import top.hondaman.manux.module.system.api.user.AdminUserApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "tradeRpcConfiguration", proxyBeanMethods = false)
@EnableFeignClients(clients = {
        BargainActivityApi.class, BargainRecordApi.class, CombinationRecordApi.class,
        CouponApi.class, DiscountActivityApi.class, RewardActivityApi.class, SeckillActivityApi.class, PointActivityApi.class,
        MemberUserApi.class, MemberPointApi.class, MemberLevelApi.class, MemberAddressApi.class, MemberConfigApi.class,
        ProductSpuApi.class, ProductSkuApi.class, ProductCommentApi.class, ProductCategoryApi.class,
        PayOrderApi.class, PayRefundApi.class, PayTransferApi.class, PayWalletApi.class,
        AdminUserApi.class, NotifyMessageSendApi.class, SocialClientApi.class, SocialUserApi.class
})
public class RpcConfiguration {
}
