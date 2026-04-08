package top.hondaman.manux.module.im.dal.dataobject.sensitiveword;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import top.hondaman.manux.framework.mybatis.core.dataobject.BaseDO;

import java.time.LocalDateTime;

/**
 * 敏感词
 */
@TableName("im_sensitive_word")
@KeySequence("im_private_message_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensitiveWordDO extends BaseDO {
	/**
	* id
	*/
	@TableId
	private Long id;

	/**
	* 敏感词内容
	*/
	private String content;

	/**
	* 是否启用
	*/
	private Boolean enabled;
}