package top.hondaman.cloud.infra.framework.codegen.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import top.hondaman.cloud.framework.common.pojo.PageResult;
import top.hondaman.cloud.framework.common.util.object.BeanUtils;
import top.hondaman.cloud.infra.framework.codegen.api.dto.CgModuleDTO;
import top.hondaman.cloud.infra.framework.codegen.api.vo.CgModuleVO;
import top.hondaman.cloud.infra.framework.codegen.mapper.CgModuleMapper;
import top.hondaman.cloud.infra.framework.codegen.service.entity.CgModuleDO;
import top.hondaman.cloud.infra.system.model.PageParam;
import top.hondaman.cloud.infra.system.model.UserInfoToken;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CgModuleService {
    @Resource
    private CgModuleMapper cgModuleMapper;

    public PageResult<CgModuleVO> getListPage(CgModuleDTO param, PageParam pageParam){
        QueryWrapper queryWrapper = new QueryWrapper<CgModuleDO>();
        queryWrapper.like(StrUtil.isNotEmpty(param.getName()),"name",param.getName());

        Page<CgModuleDO> page = PageHelper.startPage(pageParam.getPage(),pageParam.getLimit(),pageParam.getSortOrderContent())
                .doSelectPage(() -> cgModuleMapper.selectList(queryWrapper));

        List<CgModuleVO> result = BeanUtils.toBean(page.getResult(),CgModuleVO.class);
        return new PageResult(result,page.getTotal());
    }

    public String insert(CgModuleDTO param, UserInfoToken userInfo){
        CgModuleDO entity = BeanUtils.toBean(param,CgModuleDO.class);
        String id = UUID.randomUUID().toString();
        entity.setId(id);
        entity.setInsertUser(userInfo.getUserName());
        entity.setInsertTime(new Date());
        cgModuleMapper.insert(entity);

        return id;
    }
}
