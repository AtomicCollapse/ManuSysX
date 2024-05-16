package top.hondaman.cloud.module.pms.role.service;

import org.springframework.stereotype.Service;
import top.hondaman.cloud.framework.common.exception.ServiceException;
import top.hondaman.cloud.framework.common.pojo.CommonResult;
import top.hondaman.cloud.framework.common.util.object.BeanUtils;
import top.hondaman.cloud.infra.system.model.UserInfo;
import top.hondaman.cloud.module.pms.role.dto.PmsUserInfoParam;
import top.hondaman.cloud.module.pms.role.mapper.PmsUserInfoMapper;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Service
public class PmsUserInfoService {

    @Resource
    private PmsUserInfoMapper mapper;

    public String insert(PmsUserInfoParam param,String userId){
        String id = UUID.randomUUID().toString();
        UserInfo userInfo = BeanUtils.toBean(param,UserInfo.class);
        userInfo.setId(id);
        userInfo.setInsertUser(userId);
        userInfo.setInsertTime(new Date());

        mapper.insert(userInfo);
        return id;
    }

    public void update(PmsUserInfoParam param,String userId){
        UserInfo data = mapper.selectById(param.getId());
        if(data == null){
            throw new ServiceException(400,"参数错误");
        }
    }
}
