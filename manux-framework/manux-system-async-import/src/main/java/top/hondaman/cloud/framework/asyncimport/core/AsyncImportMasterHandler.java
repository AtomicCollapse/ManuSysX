package top.hondaman.cloud.framework.asyncimport.core;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.hondaman.cloud.framework.common.exception.ServerException;
import top.hondaman.cloud.framework.asyncimport.api.vo.AsyncImportTaskVO;
import top.hondaman.cloud.framework.asyncimport.enums.AsyncImportTaskStatusEnum;
import top.hondaman.cloud.framework.asyncimport.enums.MQConstants;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AsyncImportMasterHandler {

    /**
     * Spring自动注入，获取所有AsyncImportHandler接口实现类
     */
    @Autowired(required = false)
    private List<AsyncImportHandler> handlerList;


    public List<String> testGetHandlerList(){
        return handlerList.stream()
                .map(item -> item.getClass().toString())
                .collect(Collectors.toList());
    }

    @RabbitListener(queues = MQConstants.ASYNC_IMPORT)
    public void handleMessage(AsyncImportTaskVO task){
        String systemCode = task.getSystemCode();
        String taskCode = task.getTaskCode();
        String taskStatus = task.getStatus();
        AsyncImportHandler handler = getHandlerByCode(systemCode,taskCode);
        if(handler == null){
            throw new ServerException(400,String.format("任务编码[%s_%s]未获取到业务处理器",systemCode,taskCode));
        }
        if(StrUtil.isEmpty(taskStatus)){
            throw new ServerException(400,String.format("任务编码[%s_%s]解析到任务状态为空",systemCode,taskCode));
        }
        if (AsyncImportTaskStatusEnum.STATUS_CHECK_00.getValue().equals(taskStatus)){
            //开始执行业务处理器的校验方法
            handler.checkData();
        }else if(AsyncImportTaskStatusEnum.STATUS_PERSIST_00.getValue().equals(taskStatus)){
            //开始执行业务处理器的持久化方法
            handler.persistData();
        }
    }

    /**
     * 根据业务主键返回对应的处理器类
     * @return
     */
    private AsyncImportHandler getHandlerByCode(String systemCode,String taskCode){
        for (AsyncImportHandler item : handlerList){
            if(StrUtil.isNotEmpty(item.getSystemCode()) && StrUtil.isNotEmpty(item.getTaskCode())){
                if(systemCode.equals(item.getSystemCode()) && taskCode.equals(item.getTaskCode())){
                    return item;
                }
            }
        }
        return null;
    }
}
