package top.hondaman.cloud.module.pms.role.service;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import top.hondaman.cloud.framework.common.exception.ServiceException;
import top.hondaman.cloud.framework.common.util.object.BeanUtils;
import top.hondaman.cloud.framework.redis.oauth2.dto.OAuth2AccessTokenDto;
import top.hondaman.cloud.framework.redis.oauth2.service.OAuth2TokenService;
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
    @Resource
    private OAuth2TokenService oAuth2TokenService;

    public String insert(PmsUserInfoParam param){
        String id = UUID.randomUUID().toString();
        UserInfo userInfo = BeanUtils.toBean(param,UserInfo.class);
        userInfo.setId(id);
        userInfo.setInsertUser("system");
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

    public void update(PmsUserInfoParam param,String accessToken){
        UserInfo data = mapper.selectById(param.getId());
        if(data == null){
            throw new ServiceException(BAD_REQUEST,"账号id不存在");
        }
        data.setUpdateUser(oAuth2TokenService.getUserInfo(accessToken).getUserId());
        data.setUpdateTime(new Date());
        mapper.updateById(data);
    }

    public String doLogin(PmsUserInfoParam param){
        UserInfo userInfo = BeanUtils.toBean(param,UserInfo.class);
        QueryWrapper<UserInfo> wrapper = new QueryWrapper();
        wrapper.eq("user_name",userInfo.getUserName());

        if(mapper.selectOne(wrapper) == null){
            throw new ServiceException(400,"用户名不存在");
        }

        wrapper.eq("password",new Digester(DigestAlgorithm.MD5).digestHex(userInfo.getPassword()));
        UserInfo loginUser = mapper.selectOne(wrapper);
        if(loginUser == null){
            throw new ServiceException(400,"密码错误");
        }

        OAuth2AccessTokenDto oAuth2AccessTokenDto = oAuth2TokenService.createAccessToken(loginUser.getId(),loginUser.getUserType(),loginUser.getUserName());

        return oAuth2AccessTokenDto.getAccessToken();
    }

    public OAuth2AccessTokenDto doLogout(String accessToken){

        return oAuth2TokenService.removeAccessToken(accessToken);
    }
}
