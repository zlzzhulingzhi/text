package cn.qbs.wa.train.screen.pojo.screen;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author makejava
 * @since 2022-10-09 17:04:42
 */
@Data
public class NoticeVO implements Serializable {

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "标签(new-最新 urgent-紧急)")
    private String label;

    @ApiModelProperty(value = "发布时间")
    private LocalDate publishDate;

    @ApiModelProperty(value = "停留时长(单位秒，0代表永久停留)")
    private Integer duration;

}
