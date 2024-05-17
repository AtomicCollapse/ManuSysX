package top.hondaman.cloud.module.pms.role.service;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import top.hondaman.cloud.framework.common.exception.ServiceException;
import top.hondaman.cloud.framework.common.util.object.BeanUtils;
import top.hondaman.cloud.infra.system.model.UserInfo;
import top.hondaman.cloud.module.pms.role.dto.PmsUserInfoParam;
import top.hondaman.cloud.module.pms.role.mapper.PmsUserInfoMapper;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

import static top.hondaman.cloud.framework.common.exception.enums.GlobalErrorCodeConstants.BAD_REQUEST;

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

        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name",userInfo.getUserName());
        if(mapper.selectOne(wrapper) != null){
            throw new ServiceException(BAD_REQUEST,"账号已存在");
        }

        //MD5 加密
        userInfo.setPassword(new Digester(DigestAlgorithm.MD5).digestHex(userInfo.getPassword()));

        mapper.insert(userInfo);
        return id;
    }

    public void update(PmsUserInfoParam param,String userId){
        UserInfo data = mapper.selectById(param.getId());
        if(data == null){
            throw new ServiceException(BAD_REQUEST,"账号id不存在");
        }
        data.setUpdateUser(userId);
        data.setUpdateTime(new Date());
        mapper.updateById(data);
    }
}
