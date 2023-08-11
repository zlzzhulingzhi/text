package cn.qbs.wa.teach.question.pojo.paper;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;


/**
 * 试卷搜索查询参数
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 18:45:25
 */
@Data
public class PaperSearchRequest {

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

    @ApiModelProperty(value = "排序属性")
    private String sortField = "id";

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

    public String getSortField() {
        /*if (StringUtils.isNotBlank(sortField)) {
            sortField = StringUtils.camelToUnderline(sortField);
        }*/

        if (StringUtils.isNotBlank(sortField) && !ArrayUtils.contains(SORT_FIELD_NAMES, sortField)) {
            throw new IllegalParamsException("排序属性无效！");
        }
        return sortField;
    }

    public String getSortOrder() {
        if (StringUtils.isNotBlank(sortOrder) && sortOrder.equalsIgnoreCase("asc")) {
            return sortOrder;
        }
        return "desc";
    }

}

