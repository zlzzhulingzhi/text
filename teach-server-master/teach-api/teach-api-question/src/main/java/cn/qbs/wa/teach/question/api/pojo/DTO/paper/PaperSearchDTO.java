package cn.qbs.wa.teach.question.api.pojo.DTO.paper;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


/**
 * 试卷搜索查询参数
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-29 09:45:25
 */
@Data
public class PaperSearchDTO {

    public static final int MAX_SIZE = 1000;

    /**
     * 支持排序的属性数组
     */
    private static final String[] SORT_FIELD_NAMES = {"id", "createTime", "updateTime"};

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "试题分类ID列表")
    private List<Long> categoryIds;

    @ApiModelProperty(value = "搜索关键字")
    private String keyword;

    @ApiModelProperty(value = "每页数量")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    private Integer pageNum;

    @Setter(AccessLevel.NONE)
    @ApiModelProperty(value = "排序属性")
    private String sortField;

    @Setter(AccessLevel.NONE)
    @ApiModelProperty(value = "排序正序/倒序")
    private String sortOrder;


    public Integer getPageSize() {
        if (pageSize == null || pageSize < 0) {
            return 10;
        }
        if (pageSize > MAX_SIZE) {
            return MAX_SIZE;
        }
        return pageSize;
    }

    public Integer getPageNum() {
        if (pageNum == null || pageNum < 0) {
            return 1;
        }
        return pageNum;
    }

    public void orderByAsc(String sortField) {
        this.setSortField(sortField);
        this.sortOrder = "asc";
    }

    public void orderByDesc(String sortField) {
        this.setSortField(sortField);
        this.sortOrder = "desc";
    }

    private void setSortField(String sortField) {
        if (StringUtils.isNotBlank(sortField) && !ArrayUtils.contains(SORT_FIELD_NAMES, sortField)) {
            throw new IllegalParamsException("排序属性无效！");
        }
        this.sortField = sortField;
    }

}

