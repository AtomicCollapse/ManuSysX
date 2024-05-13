package top.hondaman.cloud.framework.common.pojo;

import lombok.Data;
import java.io.Serializable;

/**
 * 排序信息
 *
 * @author zyzhu
 */
@Data
public class SortParam implements Serializable {
    private static final long serialVersionUID = 1L;
    /***
     * property 数据库对应的属性
     */
    private String name;
    /***
     * 排序的方向
     */
    private Order order;


    /***
     * 拼接成功后的内容
     *
     * @return
     */
    public String getContent() {
        String sort = this.name + " " + this.order.name();
        return sort;
    }


    public static SortParam valueOf(String val) {
        if (val == null || "".equals(val.trim())) {
            return null;
        }

        // replace 处理 sql 注入
        String[] sort = val.replace("'", "''").split(";");
        SortParam param = new SortParam();
        param.setName(sort[0]);
        if (sort.length > 1) {
            param.setOrder(Order.fromString(sort[1]));
        } else {
            param.setOrder(Order.ASC);
        }
        return param;
    }


    /***
     * 排序的方向
     */
    enum Order {
        ASC,
        DESC;
        public static Order fromString(String value) {
            try {
                return Order.valueOf(value.toUpperCase());
            } catch (Exception e) {
                return Order.ASC;
            }
        }
    }
}