package cn.qbs.wa.teach.course.standard.pojo.courselivelink;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.teach.course.common.entity.CourseLiveLink;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-29 14:43:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseLiveLinkDetailResponse extends CourseLiveLink {

    private static final long serialVersionUID = -8407418554047915384L;

    @ApiModelProperty(value = "直播名称")
    private String liveName;

    @ApiModelProperty(value = "【课时管理】")
    private Integer courseManage;

    @ApiModelProperty(value = "直播开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "直播结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "0 不能回放 1可以回放")
    private Integer playbacked;

    @ApiModelProperty(value = "播放状态 0未直播 1直播中 2直播结束")
    private Integer playStatus;

    @ApiModelProperty(value = "回放地址")
    List<String> playbackUrls;
}

