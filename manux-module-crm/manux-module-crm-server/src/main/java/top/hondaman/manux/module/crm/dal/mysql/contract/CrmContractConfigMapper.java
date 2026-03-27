package top.hondaman.manux.module.crm.dal.mysql.contract;

import top.hondaman.manux.framework.mybatis.core.mapper.BaseMapperX;
import top.hondaman.manux.framework.mybatis.core.query.QueryWrapperX;
import top.hondaman.manux.module.crm.dal.dataobject.contract.CrmContractConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 合同配置 Mapper
 *
 * @author Wanwan
 */
@Mapper
public interface CrmContractConfigMapper extends BaseMapperX<CrmContractConfigDO> {

    default CrmContractConfigDO selectOne() {
        return selectOne(new QueryWrapperX<CrmContractConfigDO>().limitN(1));
    }

}