package cn.qbs.wa.teach.organization.pojo.news;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 新闻(News)创建新闻参数
 *
 * @author makejava
 * @since 2022-01-18 11:30:47
 */
@Data
public class NewsAddRequest {

    @ApiModelProperty(value = "新闻标题")
    private String title;

    @ApiModelProperty(value = "新闻类型 0 系统新闻 1外部新闻")
    private Integer type;

    @ApiModelProperty(value = "打开链接")
    private String openUrl;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "新闻内容")
    private String content;

    @ApiModelProperty(value = "封面图片地址")
    private String coverUrl;

    @ApiModelProperty(value = "置顶排序")
    private Integer sort;

    @ApiModelProperty(value = "上架状态 0 下架 1上架")
    private Integer shelfStatus;

    @ApiModelProperty(value = "发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime publishTime;

    @ApiModelProperty(value = "新闻来源")
    private String newsSource;


    @ApiModelProperty(value = "分类id数组")
    private List<Long> categoryIdList;

}

