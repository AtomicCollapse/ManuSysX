package top.hondaman.cloud.infra.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import top.hondaman.cloud.framework.common.pojo.BasicModel;

@Data
@TableName("t_infra_user_info")
public class UserInfo extends BasicModel {
    private String userName;
    private String password;
    private String email;
    private Integer userType;
    @TableField(exist = false)
    private String tradeCode;
}
