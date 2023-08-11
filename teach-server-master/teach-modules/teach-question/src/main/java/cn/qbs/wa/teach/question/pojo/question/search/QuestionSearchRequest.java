package cn.qbs.wa.teach.question.pojo.question.search;

import cn.qbs.wa.teach.common.core.domain.SortItem;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 试题(Question)搜索查询参数
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 18:45:25
 */
@Data
public class QuestionSearchRequest {

    public static final int MAX_SIZE = 1000;

    /**
     * 支持排序的属性数组
     */
    private static final String[] SORT_FIELD_NAMES = {"id", "createTime", "updateTime"};


    @ApiModelProperty(value = "题型ID列表")
    private List<Integer> questionTypeIds;

    @ApiModelProperty(value = "难度ID列表")
    private List<Integer> difficultyIds;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "试题分类ID列表")
    private List<Long> categoryIds;

    @ApiModelProperty(value = "试题分类ID")
    private Long categoryId;

    @ApiModelProperty(value = "题目状态")
    private Integer enabled;

    @ApiModelProperty(value = "搜索关键字")
    private String keyword;

    @ApiModelProperty(value = "每页数量")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    private Integer pageNum;

    @ApiModelProperty(value = "排序项列表")
    private List<SortItem> sortItemList;

    private static final List<SortItem> DEFAULT_SORT_ITEM_LIST = new ArrayList<SortItem>(){{
        add(new SortItem("id", "desc"));
    }};


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

    public void setSortItemList(List<SortItem> sortItemList) {
        if (CollectionUtils.isNotEmpty(sortItemList)) {
            for (Iterator<SortItem> iterator = sortItemList.iterator(); iterator.hasNext(); ) {
                SortItem sortItem = iterator.next();
                if (StringUtils.isBlank(sortItem.getAttr()) || !ArrayUtils.contains(SORT_FIELD_NAMES, sortItem.getAttr())) {
                    throw new IllegalParamsException("排序属性无效！");
//                    iterator.remove();
                } else {
                    if (StringUtils.isBlank(sortItem.getOrder()) || !sortItem.getOrder().equalsIgnoreCase("asc")) {
                        sortItem.setOrder("desc");
                    }
                }
            }
            this.sortItemList = sortItemList.stream().distinct().collect(Collectors.toList());
        }
    }

    public List<SortItem> getSortItemList() {
        if (CollectionUtils.isEmpty(sortItemList)) {
            return DEFAULT_SORT_ITEM_LIST;
        }
        return sortItemList;
    }
}

