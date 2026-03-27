package top.hondaman.manux.module.pay.convert.wallet;

import top.hondaman.manux.framework.common.pojo.PageResult;
import top.hondaman.manux.module.pay.controller.admin.wallet.vo.wallet.PayWalletRespVO;
import top.hondaman.manux.module.pay.controller.app.wallet.vo.wallet.AppPayWalletRespVO;
import top.hondaman.manux.module.pay.dal.dataobject.wallet.PayWalletDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PayWalletConvert {

    PayWalletConvert INSTANCE = Mappers.getMapper(PayWalletConvert.class);

    AppPayWalletRespVO convert(PayWalletDO bean);

    PayWalletRespVO convert02(PayWalletDO bean);

    PageResult<PayWalletRespVO> convertPage(PageResult<PayWalletDO> page);

}
