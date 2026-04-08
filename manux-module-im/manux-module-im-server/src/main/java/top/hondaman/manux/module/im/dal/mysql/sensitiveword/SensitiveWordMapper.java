package top.hondaman.manux.module.im.dal.mysql.sensitiveword;

import org.apache.ibatis.annotations.Mapper;
import top.hondaman.manux.framework.mybatis.core.mapper.BaseMapperX;
import top.hondaman.manux.module.im.dal.dataobject.sensitiveword.SensitiveWordDO;

@Mapper
public interface SensitiveWordMapper extends BaseMapperX<SensitiveWordDO> {
	
}