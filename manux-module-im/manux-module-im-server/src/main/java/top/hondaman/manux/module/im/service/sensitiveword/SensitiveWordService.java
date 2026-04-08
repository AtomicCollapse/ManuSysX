package top.hondaman.manux.module.im.service.sensitiveword;

import com.baomidou.mybatisplus.extension.service.IService;
import top.hondaman.manux.module.im.dal.dataobject.sensitiveword.SensitiveWordDO;

import java.util.List;

public interface SensitiveWordService extends IService<SensitiveWordDO> {

    /**
     * 查询所有开启的敏感词
     * @return
     */
    List<String> findAllEnabledWords();
}
