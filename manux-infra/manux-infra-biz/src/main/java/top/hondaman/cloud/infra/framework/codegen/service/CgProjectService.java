package top.hondaman.cloud.infra.framework.codegen.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import top.hondaman.cloud.framework.common.pojo.PageResult;
import top.hondaman.cloud.framework.common.util.object.BeanUtils;
import top.hondaman.cloud.infra.framework.codegen.api.dto.CgProjectDTO;
import top.hondaman.cloud.infra.framework.codegen.api.vo.CgProjectVO;
import top.hondaman.cloud.infra.framework.codegen.mapper.CgProjectMapper;
import top.hondaman.cloud.infra.framework.codegen.service.entity.CgProjectDO;
import top.hondaman.cloud.infra.system.model.PageParam;
import top.hondaman.cloud.infra.system.model.UserInfoToken;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CgProjectService {
    @Resource
    private CgProjectMapper cgProjectMapper;

    public PageResult<CgProjectVO> getListPage(CgProjectDTO param, PageParam pageParam){
        QueryWrapper queryWrapper = new QueryWrapper<CgProjectDO>();
        queryWrapper.like(StrUtil.isNotEmpty(param.getName()),"name",param.getName());

        Page<CgProjectDO> page = PageHelper.startPage(pageParam.getPage(),pageParam.getLimit(),pageParam.getSortOrderContent())
                .doSelectPage(() -> cgProjectMapper.selectList(queryWrapper));

        List<CgProjectVO> result = BeanUtils.toBean(page.getResult(),CgProjectVO.class);
        return new PageResult(result,page.getTotal());
    }

    public String insert(CgProjectDTO param, UserInfoToken userInfo){
        CgProjectDO entity = BeanUtils.toBean(param,CgProjectDO.class);
        String id = UUID.randomUUID().toString();
        entity.setId(id);
        entity.setInsertUser(userInfo.getUserName());
        entity.setInsertTime(new Date());
        cgProjectMapper.insert(entity);

        return id;
    }
}
