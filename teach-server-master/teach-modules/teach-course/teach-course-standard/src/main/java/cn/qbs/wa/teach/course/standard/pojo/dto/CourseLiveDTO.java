package cn.qbs.wa.teach.course.standard.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yjx
 */
@Data
public class CourseLiveDTO {

    @ApiModelProperty(value = "直播开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "直播结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "直播名称")
    private String liveName;

    @ApiModelProperty(value = "0 不能回放 1可以回放")
    private Integer playbacked;

    @ApiModelProperty(value = "播放状态 0未直播 1直播中 2直播结束")
    private Integer playStatus;

    @ApiModelProperty(value = "回放地址")
    List<String> playbackUrls;

    @ApiModelProperty(value = "直播总时长(秒)")
    private Long totalDuration;
}
