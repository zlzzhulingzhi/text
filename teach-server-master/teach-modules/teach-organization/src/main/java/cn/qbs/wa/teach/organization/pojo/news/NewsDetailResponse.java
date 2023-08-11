package cn.qbs.wa.teach.organization.pojo.news;

import cn.qbs.wa.teach.organization.entity.News;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 新闻(News)新闻详情
 *
 * @author makejava
 * @since 2022-01-18 11:30:48
 */
@Data
public class NewsDetailResponse extends News {

    @ApiModelProperty(value = "分类id数组")
    private List<Long> categoryIdList;
}

