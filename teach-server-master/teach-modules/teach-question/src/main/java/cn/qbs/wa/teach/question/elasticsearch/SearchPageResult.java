package cn.qbs.wa.teach.question.elasticsearch;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 搜索分页结果类
 * @Author zcm
 * @Date 2021/11/4 15:16
 * @Version 1.0
 */
@Data
public class SearchPageResult<T> {

    /**
     * 当前页码
     */
    @ApiModelProperty(value = "当前页码")
    private Integer pageNum;

    /**
     * 每页数量
     */
    @ApiModelProperty(value = "每页数量")
    private Integer pageSize;

    /**
     * 总记录数
     */
    @ApiModelProperty(value = "总记录数")
    private Long total;

    /**
     * 数据集合
     */
    @ApiModelProperty(value = "每页数量")
    private List<T> list;

    /**
     * 总页数
     */
    public Long getTotalPage() {
        if (total == null || pageSize == null || total <= 0 || pageSize <= 0) {
            return null;
        }
        return total % pageSize != 0 ? total / pageSize + 1 : total / pageSize;
    }

}
