package top.hondaman.manux.module.ai.dal.mysql.write;

import top.hondaman.manux.framework.common.pojo.PageResult;
import top.hondaman.manux.framework.mybatis.core.mapper.BaseMapperX;
import top.hondaman.manux.framework.mybatis.core.query.LambdaQueryWrapperX;
import top.hondaman.manux.module.ai.controller.admin.write.vo.AiWritePageReqVO;
import top.hondaman.manux.module.ai.dal.dataobject.write.AiWriteDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * AI 写作 Mapper
 *
 * @author xiaoxin
 */
@Mapper
public interface AiWriteMapper extends BaseMapperX<AiWriteDO> {

    default PageResult<AiWriteDO> selectPage(AiWritePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AiWriteDO>()
                .eqIfPresent(AiWriteDO::getUserId, reqVO.getUserId())
                .eqIfPresent(AiWriteDO::getType, reqVO.getType())
                .eqIfPresent(AiWriteDO::getPlatform, reqVO.getPlatform())
                .betweenIfPresent(AiWriteDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(AiWriteDO::getId));
    }

}
