package top.hondaman.manux.module.pay.api.transfer;

import top.hondaman.manux.framework.common.pojo.CommonResult;
import top.hondaman.manux.framework.common.util.object.BeanUtils;
import top.hondaman.manux.module.pay.api.transfer.dto.PayTransferCreateReqDTO;
import top.hondaman.manux.module.pay.api.transfer.dto.PayTransferCreateRespDTO;
import top.hondaman.manux.module.pay.api.transfer.dto.PayTransferRespDTO;
import top.hondaman.manux.module.pay.dal.dataobject.channel.PayChannelDO;
import top.hondaman.manux.module.pay.dal.dataobject.transfer.PayTransferDO;
import top.hondaman.manux.module.pay.framework.pay.core.client.impl.weixin.WxPayClientConfig;
import top.hondaman.manux.module.pay.service.channel.PayChannelService;
import top.hondaman.manux.module.pay.service.transfer.PayTransferService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

import static top.hondaman.manux.framework.common.pojo.CommonResult.success;

/**
 * 转账单 API 实现类
 *
 * @author jason
 */
@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class PayTransferApiImpl implements PayTransferApi {

    @Resource
    private PayTransferService payTransferService;
    @Resource
    private PayChannelService payChannelService;

    @Override
    public CommonResult<PayTransferCreateRespDTO> createTransfer(PayTransferCreateReqDTO reqDTO) {
        return success(payTransferService.createTransfer(reqDTO));
    }

    @Override
    public CommonResult<PayTransferRespDTO> getTransfer(Long id) {
        PayTransferDO transfer = payTransferService.getTransfer(id);
        if (transfer == null) {
            return null;
        }
        PayChannelDO channel = payChannelService.getChannel(transfer.getChannelId());
        String mchId = null;
        if (channel != null && channel.getConfig() instanceof WxPayClientConfig) {
            mchId = ((WxPayClientConfig) channel.getConfig()).getMchId();
        }
        return success(BeanUtils.toBean(transfer, PayTransferRespDTO.class).setChannelMchId(mchId));
    }

}
