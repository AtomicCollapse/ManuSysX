package top.hondaman.cloud.infra.framework.codegen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.hondaman.cloud.infra.framework.codegen.service.entity.CgDataSourceDO;

@Mapper
public interface CgDataSourceMapper extends BaseMapper<CgDataSourceDO> {
}
