package top.hondaman.cloud.infra.framework.codegen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.hondaman.cloud.infra.framework.codegen.service.entity.CgModuleDO;

@Mapper
public interface CgModuleMapper extends BaseMapper<CgModuleDO> {
}
