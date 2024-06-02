package top.hondaman.cloud.infra.asyncImport.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import top.hondaman.cloud.framework.common.exception.ServiceException;
import top.hondaman.cloud.framework.common.pojo.CommonResult;
import top.hondaman.cloud.framework.common.util.object.BeanUtils;
import top.hondaman.cloud.infra.asyncImport.dto.ImportTaskParam;
import top.hondaman.cloud.infra.asyncImport.mapper.ImportConfigMapper;
import top.hondaman.cloud.infra.asyncImport.mapper.ImportTaskMapper;
import top.hondaman.cloud.infra.asyncImport.model.ImportConfig;
import top.hondaman.cloud.infra.asyncImport.model.ImportTask;
import top.hondaman.cloud.infra.file.controller.FileApi;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
public class AsyncImportService {
    @Resource
    private ImportTaskMapper importTaskMapper;
    @Resource
    private ImportConfigMapper importConfigMapper;
    @Resource
    private FileApi fileApi;


    public String insertTask(ImportTaskParam param,String userId) throws IOException {
        /**
         * 校验导入任务是否配置
         */
        QueryWrapper<ImportConfig> queryWrapper = new QueryWrapper();
        queryWrapper
                .eq("system_code",param.getSystemCode())
                .eq("task_code",param.getTaskCode());
        if(importConfigMapper.selectOne(queryWrapper) == null){
            throw new ServiceException(400,"任务类型未配置");
        }

        /**
         * 文件上传
         */
        fileApi.uploadFile(param.getFile().getBytes());


        /**
         * 插入导入任务
         */
        ImportTask importTask = BeanUtils.toBean(param,ImportTask.class);
        importTask.setId(UUID.randomUUID().toString());
        importTask.setInsertTime(new Date());
        importTask.setInsertUser(userId);
        importTaskMapper.insert(importTask);

        return importTask.getId();
    }
}
