package cn.qbs.wa.teach.common.live.pojo;

import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/12/15 9:09
 */
@Data
public class LiveAddressResponse {

    //@ApiModelProperty(value = "流名称")
    private String streamName;

    // @ApiModelProperty(value = "rtmp推流地址")
    private String pushRtmpUrl;

    //@ApiModelProperty(value = "webrtc推流地址")
    private String pushWebrtcUrl;

    //@ApiModelProperty(value = "obs推流")
    private String pushObsUrl;

    // @ApiModelProperty(value = "obs推流密钥")
    private String pushObsSecret;

    //@ApiModelProperty(value = "rtmp拉流地址")
    private String pullRtmpUrl;

    // @ApiModelProperty(value = "flv拉流地址")
    private String pullFlvUrl;

    //@ApiModelProperty(value = "m3u8拉流地址")
    private String pullM3u8Url;

    //@ApiModelProperty(value = "udp拉流地址")
    private String pullUdpUrl;
}
