package top.hondaman.manux.module.im.service.sensitiveword;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.hondaman.manux.module.im.dal.dataobject.sensitiveword.SensitiveWordDO;
import top.hondaman.manux.module.im.dal.mysql.sensitiveword.SensitiveWordMapper;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensitiveWordServiceImpl extends ServiceImpl<SensitiveWordMapper, SensitiveWordDO> implements
    SensitiveWordService {

    @Override
    public List<String> findAllEnabledWords() {
        LambdaQueryWrapper<SensitiveWordDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SensitiveWordDO::getEnabled,true);
        wrapper.select(SensitiveWordDO::getContent);
        List<SensitiveWordDO> words = this.list(wrapper);
        return words.stream().map(SensitiveWordDO::getContent).collect(Collectors.toList());
    }
}
