package cn.qbs.wa.teach.course.live.api.pojo.DTO.liveshow;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/12/29 15:43
 */
@Data
public class LiveShowAddResultDTO {


    @ApiModelProperty(value = "直播房间id(最大长度为2的32次方)")
    private Long roomId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户名称")
    private String userName;

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

    @ApiModelProperty(value = "用户签名")
    private String userSig;

}
