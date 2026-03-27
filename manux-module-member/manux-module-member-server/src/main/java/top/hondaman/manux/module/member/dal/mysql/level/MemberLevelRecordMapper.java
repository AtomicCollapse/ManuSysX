package top.hondaman.manux.module.member.dal.mysql.level;

import top.hondaman.manux.framework.common.pojo.PageResult;
import top.hondaman.manux.framework.mybatis.core.mapper.BaseMapperX;
import top.hondaman.manux.framework.mybatis.core.query.LambdaQueryWrapperX;
import top.hondaman.manux.module.member.controller.admin.level.vo.record.MemberLevelRecordPageReqVO;
import top.hondaman.manux.module.member.dal.dataobject.level.MemberLevelRecordDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员等级记录 Mapper
 *
 * @author owen
 */
@Mapper
public interface MemberLevelRecordMapper extends BaseMapperX<MemberLevelRecordDO> {

    default PageResult<MemberLevelRecordDO> selectPage(MemberLevelRecordPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MemberLevelRecordDO>()
                .eqIfPresent(MemberLevelRecordDO::getUserId, reqVO.getUserId())
                .eqIfPresent(MemberLevelRecordDO::getLevelId, reqVO.getLevelId())
                .betweenIfPresent(MemberLevelRecordDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MemberLevelRecordDO::getId));
    }

}
