package top.hondaman.cloud.infra.asyncImport.service;

import org.springframework.stereotype.Service;
import top.hondaman.cloud.framework.common.exception.ServiceException;
import top.hondaman.cloud.framework.common.util.object.BeanUtils;
import top.hondaman.cloud.framework.rabbitmq.utils.RabbitMQHelper;
import top.hondaman.cloud.framework.asyncimport.api.dto.AsyncImportTaskDTO;
import top.hondaman.cloud.framework.asyncimport.api.vo.AsyncImportConfigVO;
import top.hondaman.cloud.framework.asyncimport.api.vo.AsyncImportTaskVO;
import top.hondaman.cloud.framework.asyncimport.enums.AsyncImportTaskStatusEnum;
import top.hondaman.cloud.infra.asyncImport.mapper.AsyncImportTaskMapper;
import top.hondaman.cloud.infra.asyncImport.service.entity.AsyncImportTaskDO;
import top.hondaman.cloud.framework.asyncimport.enums.MQConstants;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Service
public class AsyncImportTaskService {
    @Resource
    private AsyncImportTaskMapper mapper;
    @Resource
    private AsyncImportConfigService configService;

    public AsyncImportTaskVO getLastTask(String systemCode,String taskCode){
        AsyncImportTaskDO query = new AsyncImportTaskDO();
        query.setSystemCode(systemCode).setTaskCode(taskCode);

        AsyncImportTaskDO result = mapper.getLastTask(query);
        AsyncImportTaskVO resultVO = BeanUtils.toBean(result,AsyncImportTaskVO.class);
        return resultVO;
    }

    public String insertTask(AsyncImportTaskDTO dto){
        AsyncImportConfigVO configVO = configService.getConfigByCode(dto.getSystemCode(),dto.getTaskCode());

        /**
         * 判断是否有任务正在执行
         */
        AsyncImportTaskVO lastTask = getLastTask(dto.getSystemCode(),dto.getTaskCode());
        //若 【无任务|导入异常|导入完成】则重新插入任务
        if(lastTask == null || (
                    lastTask != null && (
                            AsyncImportTaskStatusEnum.STATUS_PERSIST_S1.getValue().equals(lastTask.getStatus())
                            || AsyncImportTaskStatusEnum.STATUS_PERSIST_E1.getValue().equals(lastTask.getStatus())
                    )
            )
        ){
            //插入导入任务
            AsyncImportTaskDO entity = BeanUtils.toBean(dto,AsyncImportTaskDO.class);
            entity.setId(UUID.randomUUID().toString())
                    .setInsertTime(new Date())
                    .setInsertUser("system");
            entity.setStatus(AsyncImportTaskStatusEnum.STATUS_CHECK_00.getValue());
            mapper.insert(entity);
            //导入任务队列入队
            /**
             * 测试
             */
            String queueName = MQConstants.ASYNC_IMPORT;
            RabbitMQHelper.sendMessage(queueName,BeanUtils.toBean(entity,AsyncImportTaskVO.class));

            return entity.getId();
        }else{
            throw new ServiceException(400,String.format("系统编码[%s],任务编码[%s],当前状态不可插入任务",dto.getSystemCode(),dto.getTaskCode()));
        }
    }

    public void updateTask(AsyncImportTaskDTO dto){
        AsyncImportTaskDO query = BeanUtils.toBean(dto,AsyncImportTaskDO.class);
        query.setUpdateTime(new Date()).setUpdateUser("system");
        mapper.updateById(query);
    }
}
