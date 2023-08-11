package cn.qbs.wa.train.screen.pojo.screenbanner;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

/**
 * 大屏-宣传栏(ScreenBanner)更新大屏-宣传栏参数
 *
 * @author makejava
 * @since 2022-10-18 14:17:03
 */
@Data
public class ScreenBannerUpdateRequest {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "文件类型(pic-图片 video-视频)")
    @NotEmpty(message = "文件类型不能为空")
    private String fileType;

    @ApiModelProperty(value = "文件路径")
    @NotEmpty(message = "文件路径不能为空")
    private String fileUrl;

    @ApiModelProperty(value = "停留时长(单位秒，0代表永久停留)")
    private Integer duration;

    @ApiModelProperty(value = "排序号")
    private Integer sort;

    @ApiModelProperty(value = "展示状态(0-不展示 1-展示)")
    private Integer display;

}

