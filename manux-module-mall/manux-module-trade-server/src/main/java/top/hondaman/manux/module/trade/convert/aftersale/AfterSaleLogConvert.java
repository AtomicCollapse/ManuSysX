package top.hondaman.manux.module.trade.convert.aftersale;

import top.hondaman.manux.module.trade.dal.dataobject.aftersale.AfterSaleLogDO;
import top.hondaman.manux.module.trade.service.aftersale.bo.AfterSaleLogCreateReqBO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AfterSaleLogConvert {

    AfterSaleLogConvert INSTANCE = Mappers.getMapper(AfterSaleLogConvert.class);

    AfterSaleLogDO convert(AfterSaleLogCreateReqBO bean);

}
