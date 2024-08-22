package top.hondaman.cloud.infra.asyncImport.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import top.hondaman.cloud.framework.common.exception.ServiceException;
import top.hondaman.cloud.framework.common.util.object.BeanUtils;
import top.hondaman.cloud.infra.asyncImport.controller.dto.AsyncImportConfigDTO;
import top.hondaman.cloud.infra.asyncImport.controller.vo.AsyncImportConfigVO;
import top.hondaman.cloud.infra.asyncImport.mapper.AsyncImportConfigMapper;
import top.hondaman.cloud.infra.asyncImport.service.entity.AsyncImportConfigDO;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Service
public class AsyncImportConfigService {
    @Resource
    private AsyncImportConfigMapper mapper;

    public AsyncImportConfigVO getConfigByCode(String systemCode,String taskCode){
        QueryWrapper<AsyncImportConfigDO> wrapper = new QueryWrapper<>();
        wrapper
                .eq("system_code",systemCode)
                .eq("task_code",taskCode);
        AsyncImportConfigDO entity = mapper.selectOne(wrapper);
        if(entity == null){
            throw new ServiceException(400,"任务类型未配置");
        }

        AsyncImportConfigVO result = BeanUtils.toBean(entity,AsyncImportConfigVO.class);
        return result;
    }

    public String insertConfig(AsyncImportConfigDTO dto){
        AsyncImportConfigDO entity = BeanUtils.toBean(dto,AsyncImportConfigDO.class,in -> in
                .setId(UUID.randomUUID().toString())
                .setInsertTime(new Date())
                .setInsertUser("system")
        );

        mapper.insert(entity);
        return entity.getId();
    }
}
