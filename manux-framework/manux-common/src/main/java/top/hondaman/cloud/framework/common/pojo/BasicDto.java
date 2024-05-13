package top.hondaman.cloud.framework.common.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public abstract class BasicDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *  唯一键
     */
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
