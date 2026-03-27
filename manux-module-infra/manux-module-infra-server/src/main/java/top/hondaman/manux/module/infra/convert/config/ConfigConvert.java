package top.hondaman.manux.module.infra.convert.config;

import top.hondaman.manux.framework.common.pojo.PageResult;
import top.hondaman.manux.module.infra.controller.admin.config.vo.ConfigRespVO;
import top.hondaman.manux.module.infra.controller.admin.config.vo.ConfigSaveReqVO;
import top.hondaman.manux.module.infra.dal.dataobject.config.ConfigDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ConfigConvert {

    ConfigConvert INSTANCE = Mappers.getMapper(ConfigConvert.class);

    PageResult<ConfigRespVO> convertPage(PageResult<ConfigDO> page);

    List<ConfigRespVO> convertList(List<ConfigDO> list);

    @Mapping(source = "configKey", target = "key")
    ConfigRespVO convert(ConfigDO bean);

    @Mapping(source = "key", target = "configKey")
    ConfigDO convert(ConfigSaveReqVO bean);

}
