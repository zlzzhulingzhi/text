package cn.qbs.wa.train.logistics.pojo.news;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 新闻(News)分页查询参数
 *
 * @author makejava
 * @since 2022-01-18 11:30:47
 */
@Data
public class NewsPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "新闻标题")
    private String title;

    @ApiModelProperty(value = "分类id数组")
    private List<Long> categoryIdList;

    @ApiModelProperty(value = "上架状态 0 下架 1上架")
    private Integer shelfStatus;





}

