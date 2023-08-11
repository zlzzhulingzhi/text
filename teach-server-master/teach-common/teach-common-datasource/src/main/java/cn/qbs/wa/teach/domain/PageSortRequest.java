package cn.qbs.wa.teach.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * 分页排序请求类
 *
 * @Author zcm
 * @Date 2022-04-27 16:39
 * @Version 1.0
 */
@Data
public abstract class PageSortRequest {

    public static final int MAX_SIZE = 1000;

    @ApiModelProperty(value = "页码大小")
    private Integer size;

    @ApiModelProperty(value = "当前页")
    private Integer current;

    @Getter(AccessLevel.NONE)
    @ApiModelProperty(value = "排序项")
    private List<SortItem> sortItemList;


    public <T> Page<T> createMpPage() {
        Page<T> page = new Page<>(this.getCurrent(), this.getSize());
        return page;
    }

    public Integer getSize() {
        if (size == null || size < 0) {
            return 10;
        }
        if (size > MAX_SIZE) {
            return MAX_SIZE;
        }
        return size;
    }

    public Integer getCurrent() {
        if (current == null || current < 0) {
            return 1;
        }
        return current;
    }

    public List<SortItem> getSortItemList() {
        if (sortItemList != null && sortItemList.size() > 0) {
            List<String> supportSortColumnList = getSupportSortColumnList();
            if (supportSortColumnList != null && supportSortColumnList.size() > 0) {
                sortItemList.removeIf(i -> !supportSortColumnList.contains(i.getColumn()));
            }
        }
        return sortItemList;
    }

    /**
     * 支持排序的列
     * @return
     */
    protected abstract List<String> getSupportSortColumnList();

}
