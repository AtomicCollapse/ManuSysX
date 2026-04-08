package top.hondaman.manux.module.im.dal.dataobject.fileinfo;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import top.hondaman.manux.framework.mybatis.core.dataobject.BaseDO;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * im聊天文件
 */
@TableName("im_file_info")
@KeySequence("im_file_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileInfoDO extends BaseDO {

    /**
     * 文件ID
     */
    @TableId
    private Long id;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 原始文件存储路径
     */
    private String filePath;

    /**
     * 压缩文件存储路径
     */
    private String compressedPath;

    /**
     * 封面文件路径
     */
    private String coverPath;

    /**
     * 原始文件大小(字节)
     */
    private Long fileSize;

    /**
     * 上传时间
     */
    private LocalDateTime uploadTime;

    /**
     * 文件类型，枚举: FileType
     */
    private Integer fileType;

    /**
     * 是否永久存储
     */
    private Boolean isPermanent;

    /**
     * 文件MD5哈希值
     */
    private String md5;
}
