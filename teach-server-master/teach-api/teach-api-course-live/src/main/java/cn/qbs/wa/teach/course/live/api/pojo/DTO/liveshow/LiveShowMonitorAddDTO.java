package cn.qbs.wa.teach.course.live.api.pojo.DTO.liveshow;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/12/29 15:40
 */
@Data
public class LiveShowMonitorAddDTO {


    @ApiModelProperty(value = "实际开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime actualStartTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "1课程直播房间 100 监考直播房间")
    private Integer liveRoomType=100;


}
