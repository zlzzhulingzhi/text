package cn.qbs.wa.teach.exam.admin.pojo.examineeliveroom;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 考生直播房间(ExamineeLiveRoom)分页查询参数
 *
 * @author makejava
 * @since 2022-01-04 11:44:39
 */
@Data
public class ExamineeLiveRoomPageRequest extends BasePageRequest {
    
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

}

