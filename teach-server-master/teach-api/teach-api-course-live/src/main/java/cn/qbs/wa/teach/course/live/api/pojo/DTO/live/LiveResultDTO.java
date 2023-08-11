package cn.qbs.wa.teach.course.live.api.pojo.DTO.live;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/12/29 15:29
 */
@Data
public class LiveResultDTO {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "直播名称")
    private String liveName;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "0 不能回放 1可以回放")
    Integer playbacked;

    @ApiModelProperty(value = "播放状态 0未直播 1直播中 2直播结束")
    Integer playStatus;

    @ApiModelProperty(value = "回放地址")
    List<String> playbackUrls;

    @ApiModelProperty(value = "封面地址")
    private String coverUrl;

    @ApiModelProperty(value = "业务类型")
    private Integer businessType;

    @ApiModelProperty(value = "业务id")
    private Long businessId;

    @ApiModelProperty(value = "直播总时长(秒)")
    private Long totalDuration;

    @ApiModelProperty(value = "直播状态")
    private Integer status;





}
