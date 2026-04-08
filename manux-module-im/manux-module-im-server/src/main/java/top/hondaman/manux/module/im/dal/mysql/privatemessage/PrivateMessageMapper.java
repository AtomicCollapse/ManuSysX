package top.hondaman.manux.module.im.dal.mysql.privatemessage;

import org.apache.ibatis.annotations.Mapper;
import top.hondaman.manux.framework.mybatis.core.mapper.BaseMapperX;
import top.hondaman.manux.module.im.dal.dataobject.privatemessage.PrivateMessageDO;

@Mapper
public interface PrivateMessageMapper extends BaseMapperX<PrivateMessageDO> {
}
