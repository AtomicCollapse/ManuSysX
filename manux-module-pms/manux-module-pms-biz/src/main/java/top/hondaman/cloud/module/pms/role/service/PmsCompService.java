package top.hondaman.cloud.module.pms.role.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import top.hondaman.cloud.framework.common.pojo.PageResult;
import top.hondaman.cloud.framework.common.util.object.BeanUtils;
import top.hondaman.cloud.module.pms.role.dto.PmsCompDto;
import top.hondaman.cloud.module.pms.role.dto.PmsCompParam;
import top.hondaman.cloud.module.pms.role.mapper.PmsCompMapper;
import top.hondaman.cloud.module.pms.role.model.PmsComp;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PmsCompService {
    @Resource
    private PmsCompMapper pmsCompMapper;

    public PageResult<PmsCompDto> getListPage(PmsCompParam param){
        QueryWrapper queryWrapper = new QueryWrapper<PmsComp>();
        queryWrapper.like(StrUtil.isNotEmpty(param.getName()),"name",param.getName());

        Page<PmsComp> page = PageHelper.startPage(param.getPage(),param.getLimit(),param.getSortOrderContent())
                .doSelectPage(() -> pmsCompMapper.selectList(queryWrapper));

        List<PmsCompDto> dtos = BeanUtils.toBean(page.getResult(),PmsCompDto.class);
        return new PageResult(dtos,page.getTotal());
    }

    public String insert(PmsCompParam param){
        PmsComp pmsComp = BeanUtils.toBean(param,PmsComp.class);

        return null;
    }
}
