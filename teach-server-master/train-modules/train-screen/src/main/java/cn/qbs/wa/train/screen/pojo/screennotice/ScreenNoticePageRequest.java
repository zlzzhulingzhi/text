package cn.qbs.wa.train.screen.pojo.screennotice;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

import java.util.Date;

/**
 * 大屏-通知(ScreenNotice)分页查询参数
 *
 * @author makejava
 * @since 2022-10-14 15:30:33
 */
@Data
public class ScreenNoticePageRequest extends BasePageRequest {

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "标签(new-最新 urgent-紧急)")
    private String label;

    @ApiModelProperty(value = "发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishDate;

    @ApiModelProperty(value = "停留时长(单位秒，0代表永久停留)")
    private Integer duration;

    @ApiModelProperty(value = "排序号")
    private Integer sort;

    @ApiModelProperty(value = "展示状态(0-不展示 1-展示)")
    private String display;

}

