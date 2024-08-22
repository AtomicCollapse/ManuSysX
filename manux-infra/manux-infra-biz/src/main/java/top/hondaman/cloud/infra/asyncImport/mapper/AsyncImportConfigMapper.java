package top.hondaman.cloud.infra.asyncImport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.hondaman.cloud.infra.asyncImport.service.entity.AsyncImportConfigDO;

@Mapper
public interface AsyncImportConfigMapper extends BaseMapper<AsyncImportConfigDO> {

}
