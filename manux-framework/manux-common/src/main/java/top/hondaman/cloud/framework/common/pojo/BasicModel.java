package top.hondaman.cloud.framework.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Getter
public abstract class BasicModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *  唯一键
     */
    @TableId
    protected String Id;

    /**
     * 插入人
     */
    protected String insertUser;

    /**
     * 插入时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    protected Date insertTime;

    /**
     * 更新人
     */
    protected String updateUser;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    protected Date updateTime;

}
