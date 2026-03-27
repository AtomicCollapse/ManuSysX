package top.hondaman.manux.module.trade.convert.order;

import top.hondaman.manux.module.trade.dal.dataobject.order.TradeOrderLogDO;
import top.hondaman.manux.module.trade.service.order.bo.TradeOrderLogCreateReqBO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TradeOrderLogConvert {

    TradeOrderLogConvert INSTANCE = Mappers.getMapper(TradeOrderLogConvert.class);

    TradeOrderLogDO convert(TradeOrderLogCreateReqBO bean);

}
