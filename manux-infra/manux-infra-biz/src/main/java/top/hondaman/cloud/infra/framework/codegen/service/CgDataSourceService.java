package top.hondaman.cloud.infra.framework.codegen.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import top.hondaman.cloud.framework.common.pojo.PageResult;
import top.hondaman.cloud.framework.common.util.object.BeanUtils;
import top.hondaman.cloud.infra.framework.codegen.api.dto.CgDataSourceDTO;
import top.hondaman.cloud.infra.framework.codegen.api.vo.CgDataSourceVO;
import top.hondaman.cloud.infra.framework.codegen.mapper.CgDataSourceMapper;
import top.hondaman.cloud.infra.framework.codegen.service.entity.CgDataSourceDO;
import top.hondaman.cloud.infra.system.model.PageParam;
import top.hondaman.cloud.infra.system.model.UserInfoToken;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CgDataSourceService {
    @Resource
    private CgDataSourceMapper cgDataSourceMapper;


    public PageResult<CgDataSourceVO> getListPage(CgDataSourceDTO param, PageParam pageParam){
        QueryWrapper queryWrapper = new QueryWrapper<CgDataSourceDO>();
        queryWrapper.like(StrUtil.isNotEmpty(param.getName()),"name",param.getName());

        Page<CgDataSourceDO> page = PageHelper.startPage(pageParam.getPage(),pageParam.getLimit(),pageParam.getSortOrderContent())
                .doSelectPage(() -> cgDataSourceMapper.selectList(queryWrapper));

        List<CgDataSourceVO> result = BeanUtils.toBean(page.getResult(),CgDataSourceVO.class);
        return new PageResult(result,page.getTotal());
    }

    public String insert(CgDataSourceDTO param, UserInfoToken userInfo){
        CgDataSourceDO entity = BeanUtils.toBean(param,CgDataSourceDO.class);
        String id = UUID.randomUUID().toString();
        entity.setId(id);
        entity.setInsertUser(userInfo.getUserName());
        entity.setInsertTime(new Date());
        cgDataSourceMapper.insert(entity);

        return id;
    }
}
