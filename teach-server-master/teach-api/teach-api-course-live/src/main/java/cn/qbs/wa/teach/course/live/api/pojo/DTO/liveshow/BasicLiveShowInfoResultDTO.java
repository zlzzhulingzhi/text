package cn.qbs.wa.teach.course.live.api.pojo.DTO.liveshow;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 基础直播场次表(BasicLiveShow)基础直播场次表详情
 *
 * @author makejava
 * @since 2021-12-20 16:46:42
 */
@Data
public class BasicLiveShowInfoResultDTO  {


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "基础直播id")
    private Long basicLiveId;

    @ApiModelProperty(value = "流名称")
    private String streamName;

    @ApiModelProperty(value = "rtmp推流地址")
    private String pushRtmpUrl;

    @ApiModelProperty(value = "webrtc推流地址")
    private String pushWebrtcUrl;

    @ApiModelProperty(value = "obs推流")
    private String pushObsUrl;

    @ApiModelProperty(value = "obs推流密钥")
    private String pushObsSecret;

    @ApiModelProperty(value = "rtmp拉流地址")
    private String pullRtmpUrl;

    @ApiModelProperty(value = "flv拉流地址")
    private String pullFlvUrl;

    @ApiModelProperty(value = "m3u8拉流地址")
    private String pullM3u8Url;

    @ApiModelProperty(value = "udp拉流地址")
    private String pullUdpUrl;

    @ApiModelProperty(value = "0未直播 1直播中 2直播结束")
    private Integer status;

    @ApiModelProperty(value = "实际开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime actualStartTime;

    @ApiModelProperty(value = "实际结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime actualEndTime;

    @ApiModelProperty(value = "直播第几次")
    private Integer showNum;

    @ApiModelProperty(value = "直播房间id(最大长度为2的32次方)")
    private Long roomId;

    @ApiModelProperty(value = "服务商code")
    private String providerCode;

    @ApiModelProperty(value = "用户签名")
    private String userSig;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户名称")
    private String userName;

}

