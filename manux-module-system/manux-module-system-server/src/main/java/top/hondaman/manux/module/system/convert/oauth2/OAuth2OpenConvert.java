package top.hondaman.manux.module.system.convert.oauth2;

import cn.hutool.core.date.LocalDateTimeUtil;
import top.hondaman.manux.framework.common.core.KeyValue;
import top.hondaman.manux.framework.common.enums.UserTypeEnum;
import top.hondaman.manux.framework.common.util.collection.CollectionUtils;
import top.hondaman.manux.framework.common.util.object.BeanUtils;
import top.hondaman.manux.framework.security.core.util.SecurityFrameworkUtils;
import top.hondaman.manux.module.system.controller.admin.oauth2.vo.open.OAuth2OpenAccessTokenRespVO;
import top.hondaman.manux.module.system.controller.admin.oauth2.vo.open.OAuth2OpenAuthorizeInfoRespVO;
import top.hondaman.manux.module.system.controller.admin.oauth2.vo.open.OAuth2OpenCheckTokenRespVO;
import top.hondaman.manux.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import top.hondaman.manux.module.system.dal.dataobject.oauth2.OAuth2ApproveDO;
import top.hondaman.manux.module.system.dal.dataobject.oauth2.OAuth2ClientDO;
import top.hondaman.manux.module.system.util.oauth2.OAuth2Utils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface OAuth2OpenConvert {

    OAuth2OpenConvert INSTANCE = Mappers.getMapper(OAuth2OpenConvert.class);

    default OAuth2OpenAccessTokenRespVO convert(OAuth2AccessTokenDO bean) {
        OAuth2OpenAccessTokenRespVO respVO = BeanUtils.toBean(bean, OAuth2OpenAccessTokenRespVO.class);
        respVO.setTokenType(SecurityFrameworkUtils.AUTHORIZATION_BEARER.toLowerCase());
        respVO.setExpiresIn(OAuth2Utils.getExpiresIn(bean.getExpiresTime()));
        respVO.setScope(OAuth2Utils.buildScopeStr(bean.getScopes()));
        return respVO;
    }

    default OAuth2OpenCheckTokenRespVO convert2(OAuth2AccessTokenDO bean) {
        OAuth2OpenCheckTokenRespVO respVO = BeanUtils.toBean(bean, OAuth2OpenCheckTokenRespVO.class);
        respVO.setExp(LocalDateTimeUtil.toEpochMilli(bean.getExpiresTime()) / 1000L);
        respVO.setUserType(UserTypeEnum.ADMIN.getValue());
        return respVO;
    }

    default OAuth2OpenAuthorizeInfoRespVO convert(OAuth2ClientDO client, List<OAuth2ApproveDO> approves) {
        // 构建 scopes
        List<KeyValue<String, Boolean>> scopes = new ArrayList<>(client.getScopes().size());
        Map<String, OAuth2ApproveDO> approveMap = CollectionUtils.convertMap(approves, OAuth2ApproveDO::getScope);
        client.getScopes().forEach(scope -> {
            OAuth2ApproveDO approve = approveMap.get(scope);
            scopes.add(new KeyValue<>(scope, approve != null ? approve.getApproved() : false));
        });
        // 拼接返回
        return new OAuth2OpenAuthorizeInfoRespVO(
                new OAuth2OpenAuthorizeInfoRespVO.Client(client.getName(), client.getLogo()), scopes);
    }

}
