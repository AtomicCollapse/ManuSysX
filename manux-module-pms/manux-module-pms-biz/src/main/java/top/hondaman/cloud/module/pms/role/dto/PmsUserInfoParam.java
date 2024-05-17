package top.hondaman.cloud.module.pms.role.dto;

import lombok.Data;
import top.hondaman.cloud.framework.common.pojo.BasicParam;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class PmsUserInfoParam extends BasicParam {
    private String id;

    @NotEmpty(message = "用户名不能为空")
    private String userName;

    @Size(max = 30,message = "密码最长为30位")
    @NotEmpty(message = "密码不能为空")
    private String password;
}
