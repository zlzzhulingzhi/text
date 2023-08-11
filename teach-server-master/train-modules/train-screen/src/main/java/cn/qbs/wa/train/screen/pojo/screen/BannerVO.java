package cn.qbs.wa.train.screen.pojo.screen;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author makejava
 * @since 2022-10-09 17:04:41
 */
@Data
public class BannerVO implements Serializable {

    @ApiModelProperty(value = "文件类型(pic-图片 video-视频)")
    private String fileType;

    @ApiModelProperty(value = "文件路径")
    private String fileUrl;

    @ApiModelProperty(value = "停留时长(单位秒，0代表永久停留)")
    private Integer duration;
}
