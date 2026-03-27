package top.hondaman.manux.module.member.convert.point;

import top.hondaman.manux.framework.common.pojo.PageResult;
import top.hondaman.manux.framework.common.util.collection.MapUtils;
import top.hondaman.manux.module.member.controller.admin.point.vo.recrod.MemberPointRecordRespVO;
import top.hondaman.manux.module.member.controller.app.point.vo.AppMemberPointRecordRespVO;
import top.hondaman.manux.module.member.dal.dataobject.point.MemberPointRecordDO;
import top.hondaman.manux.module.member.dal.dataobject.user.MemberUserDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

import static top.hondaman.manux.framework.common.util.collection.CollectionUtils.convertMap;

/**
 * 用户积分记录 Convert
 *
 * @author QingX
 */
@Mapper
public interface MemberPointRecordConvert {

    MemberPointRecordConvert INSTANCE = Mappers.getMapper(MemberPointRecordConvert.class);

    default PageResult<MemberPointRecordRespVO> convertPage(PageResult<MemberPointRecordDO> pageResult, List<MemberUserDO> users) {
        PageResult<MemberPointRecordRespVO> voPageResult = convertPage(pageResult);
        // user 拼接
        Map<Long, MemberUserDO> userMap = convertMap(users, MemberUserDO::getId);
        voPageResult.getList().forEach(record -> MapUtils.findAndThen(userMap, record.getUserId(),
                memberUserRespDTO -> record.setNickname(memberUserRespDTO.getNickname())));
        return voPageResult;
    }
    PageResult<MemberPointRecordRespVO> convertPage(PageResult<MemberPointRecordDO> pageResult);

}
