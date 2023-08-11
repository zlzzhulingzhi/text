package cn.qbs.wa.teach.course.live.task.api.pojo.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/6/27 9:56
 */
@Data
public class BasicLiveConfigJobAddDTO {

    @ApiModelProperty(value = "直播配置id")
    private Long basicLiveConfigId;

    @ApiModelProperty(value = "基础直播id")
    private Long basicLiveId;

    @ApiModelProperty(value = "配置编码")
    private String configCode;

    @ApiModelProperty(value = "额外配置值")
    private String extraValue;

    @ApiModelProperty(value = "配置父编码")
    private String configParentCode;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;


}
