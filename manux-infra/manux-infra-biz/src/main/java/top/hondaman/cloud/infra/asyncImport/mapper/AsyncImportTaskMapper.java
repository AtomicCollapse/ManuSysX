package top.hondaman.cloud.infra.asyncImport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.hondaman.cloud.infra.asyncImport.service.entity.AsyncImportTaskDO;

@Mapper
public interface AsyncImportTaskMapper extends BaseMapper<AsyncImportTaskDO> {
    AsyncImportTaskDO getLastTask(@Param("query") AsyncImportTaskDO query);
}
