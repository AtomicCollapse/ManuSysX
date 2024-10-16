package top.hondaman.cloud.infra.system.model;

import lombok.Getter;
import lombok.Setter;
import top.hondaman.cloud.framework.common.pojo.SortParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 分页参数
 */
@Getter
@Setter
public class PageParam {
    /**
     *  当前页码
     */
    private int page;

    /**
     *  每页数量
     */
    private int limit;

    /**
     * 排序
     */
    private List<SortParam> sort;

    public String getSortOrderContent() {
        return this.getSortOrder().orElse("");
    }

    public Optional<String> getSortOrder() {
        if (this.sort == null || this.sort.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(this.sort.stream().map(sort -> sort.getContent()).collect(Collectors.joining(",")));
    }
}
