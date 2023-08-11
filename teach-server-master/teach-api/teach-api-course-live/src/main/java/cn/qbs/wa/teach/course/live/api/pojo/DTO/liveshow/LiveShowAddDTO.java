package cn.qbs.wa.teach.course.live.api.pojo.DTO.liveshow;

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
public class LiveShowAddDTO {


    @ApiModelProperty(value = "基础直播id")
    private Long basicLiveId;


    @ApiModelProperty(value = "实际开始时间")
    private LocalDateTime actualStartTime;


    @ApiModelProperty(value = "1课程直播房间 100 监考直播房间")
    private Integer liveRoomType;
}
