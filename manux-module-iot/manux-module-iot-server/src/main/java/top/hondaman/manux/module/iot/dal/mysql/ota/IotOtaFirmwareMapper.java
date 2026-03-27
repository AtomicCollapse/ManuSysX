package top.hondaman.manux.module.iot.dal.mysql.ota;

import top.hondaman.manux.framework.common.pojo.PageResult;
import top.hondaman.manux.framework.mybatis.core.mapper.BaseMapperX;
import top.hondaman.manux.framework.mybatis.core.query.LambdaQueryWrapperX;
import top.hondaman.manux.module.iot.controller.admin.ota.vo.firmware.IotOtaFirmwarePageReqVO;
import top.hondaman.manux.module.iot.dal.dataobject.ota.IotOtaFirmwareDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IotOtaFirmwareMapper extends BaseMapperX<IotOtaFirmwareDO> {

    default IotOtaFirmwareDO selectByProductIdAndVersion(Long productId, String version) {
        return selectOne(IotOtaFirmwareDO::getProductId, productId,
                IotOtaFirmwareDO::getVersion, version);
    }

    default PageResult<IotOtaFirmwareDO> selectPage(IotOtaFirmwarePageReqVO pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<IotOtaFirmwareDO>()
                .likeIfPresent(IotOtaFirmwareDO::getName, pageReqVO.getName())
                .eqIfPresent(IotOtaFirmwareDO::getProductId, pageReqVO.getProductId())
                .betweenIfPresent(IotOtaFirmwareDO::getCreateTime, pageReqVO.getCreateTime())
                .orderByDesc(IotOtaFirmwareDO::getCreateTime));
    }

}
