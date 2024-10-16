package top.hondaman.cloud.framework.common.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public abstract class BasicParam implements Serializable {
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
     * 插入时间 区间起始
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    protected Date insertTimeFrom;
    /**
     * 插入时间 区间结束
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    protected Date insertTimeTo;

    /**
     * 更新人
     */
    protected String updateUser;

    /**
     * 更新时间 区间起始
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    protected Date updateTimeFrom;
    /**
     * 更新时间 区间结束
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    protected Date updateTimeTo;
}
