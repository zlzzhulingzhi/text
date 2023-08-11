package cn.qbs.wa.train.screen.pojo.screenbanner;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 大屏-宣传栏(ScreenBanner)分页查询参数
 *
 * @author makejava
 * @since 2022-10-14 15:45:06
 */
@Data
public class ScreenBannerPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "文件类型(pic-图片 video-视频)")
    private String fileType;

    @ApiModelProperty(value = "文件路径")
    private String fileUrl;

    @ApiModelProperty(value = "停留时长(单位秒，0代表永久停留)")
    private Integer duration;

    @ApiModelProperty(value = "排序号")
    private Integer sort;

    @ApiModelProperty(value = "展示状态(0-不展示 1-展示)")
    private Integer display;

}

