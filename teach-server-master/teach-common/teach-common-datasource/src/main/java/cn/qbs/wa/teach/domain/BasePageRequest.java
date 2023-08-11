package cn.qbs.wa.teach.domain;

import cn.qbs.wa.teach.common.core.utils.SqlUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 19:52
 */
@Data
public class BasePageRequest {

    public static final int MAX_SIZE = 1000;

    @ApiModelProperty(value = "页码大小")
    private Integer size;

    @ApiModelProperty(value = "当前页")
    private Integer current;

    @ApiModelProperty(value = "排序属性")
    private String sortField;

    @ApiModelProperty(value = "排序正序/倒序")
    private String sortOrder;


    public <T> Page<T> createMpPage() {
        Page<T> page = new Page<>(this.getCurrent(), this.getSize());
        // 加入排序功能
        if (StringUtils.isNotEmpty(getSortField())) {
            SqlUtil.escapeOrderBySql(getSortField());
            page.addOrder(new OrderItem(this.getSortField(), "asc".equals(this.getSortOrder())));
        }
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

    public String getSortField() {
        if (StringUtils.isNotBlank(sortField)) {
            sortField = StringUtils.camelToUnderline(sortField);
        }
        return sortField;
    }

    public String getSortOrder() {
        if (StringUtils.isNotBlank(sortOrder) && "desc".equalsIgnoreCase(sortOrder)) {
            return sortOrder;
        }
        return "asc";
    }

}
