package cn.qbs.wa.teach.exam.admin.pojo.examineeliveroom;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * 考生直播房间(ExamineeLiveRoom)创建考生直播房间参数
 *
 * @author makejava
 * @since 2022-01-04 11:44:40
 */
@Data
public class ExamineeLiveRoomAddRequest {
    
    @ApiModelProperty(value = "机构id")
    private Long orgId;
    
    @ApiModelProperty(value = "房间id")
    private Long roomId;
    
    @ApiModelProperty(value = "考试id")
    private Long examId;
    
    @ApiModelProperty(value = "考生id")
    private Long examineeId;
    
    @ApiModelProperty(value = "考试记录id")
    private Long examineeRecordId;
    
    @ApiModelProperty(value = "备注")
    private String remark;
    
    @ApiModelProperty(value = "流名称")
    private String streamName;
    
    @ApiModelProperty(value = "rtmp推流地址")
    private String pushRtmpUrl;
    
    @ApiModelProperty(value = "webrtc推流地址")
    private String pushWebrtcUrl;
    
    @ApiModelProperty(value = "rtmp拉流地址")
    private String pullUdpUrl;
    
    @ApiModelProperty(value = "m3u8拉流地址")
    private String pullM3u8Url;
    
    @ApiModelProperty(value = "flv拉流地址")
    private String pullFlvUrl;
    
    @ApiModelProperty(value = "udp拉流地址")
    private String pullRtmpUrl;

    @ApiModelProperty(value = "实际开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "1课程直播房间 100 监考直播房间")
    private Integer LiveRoomType=100;

}

