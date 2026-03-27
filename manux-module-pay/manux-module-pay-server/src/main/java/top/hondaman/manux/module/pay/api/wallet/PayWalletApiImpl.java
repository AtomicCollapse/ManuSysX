package top.hondaman.manux.module.pay.api.wallet;

import cn.hutool.core.lang.Assert;
import top.hondaman.manux.framework.common.pojo.CommonResult;
import top.hondaman.manux.framework.common.util.object.BeanUtils;
import top.hondaman.manux.module.pay.api.wallet.dto.PayWalletAddBalanceReqDTO;
import top.hondaman.manux.module.pay.api.wallet.dto.PayWalletRespDTO;
import top.hondaman.manux.module.pay.dal.dataobject.wallet.PayWalletDO;
import top.hondaman.manux.module.pay.enums.wallet.PayWalletBizTypeEnum;
import top.hondaman.manux.module.pay.service.wallet.PayWalletService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import static top.hondaman.manux.framework.common.pojo.CommonResult.*;

/**
 * 钱包 API 实现类
 *
 * @author jason
 */
@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class PayWalletApiImpl implements PayWalletApi {

    @Resource
    private PayWalletService payWalletService;

    @Override
    public CommonResult<Boolean> addWalletBalance(PayWalletAddBalanceReqDTO reqDTO) {
        // 创建或获取钱包
        PayWalletDO wallet = payWalletService.getOrCreateWallet(reqDTO.getUserId(), reqDTO.getUserType());
        Assert.notNull(wallet, "钱包({}/{})不存在", reqDTO.getUserId(), reqDTO.getUserType());

        // 增加余额
        PayWalletBizTypeEnum bizType = PayWalletBizTypeEnum.valueOf(reqDTO.getBizType());
        payWalletService.addWalletBalance(wallet.getId(), reqDTO.getBizId(), bizType, reqDTO.getPrice());
        return success(true);
    }

    @Override
    public CommonResult<PayWalletRespDTO> getOrCreateWallet(Long userId, Integer userType) {
        PayWalletDO wallet = payWalletService.getOrCreateWallet(userId, userType);
        return success(BeanUtils.toBean(wallet, PayWalletRespDTO.class));
    }

}
