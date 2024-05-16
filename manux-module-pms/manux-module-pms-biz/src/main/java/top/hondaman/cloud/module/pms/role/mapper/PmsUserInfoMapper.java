package top.hondaman.cloud.module.pms.role.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.hondaman.cloud.infra.system.model.UserInfo;

@Mapper
public interface PmsUserInfoMapper extends BaseMapper<UserInfo> {
}
