package top.hondaman.manux.module.im.dal.mysql.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.hondaman.manux.module.im.dal.dataobject.user.IMUserDO;

@Mapper
public interface IMUserMapper extends BaseMapper<IMUserDO> {

}
