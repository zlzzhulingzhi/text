package cn.qbs.wa.teach.course.common.entity.live;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author makejava
 * @since 2021-12-29 11:07:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BasicLiveShow extends Model {

    private static final long serialVersionUID = -55676609335519507L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "基础直播id")
    private Long basicLiveId;

    @ApiModelProperty(value = "主播用户id")
    private Long userId;

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

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "直播房间id(最大长度为2的32次方)")
    private Long roomId;

    @ApiModelProperty(value = "服务商code")
    private String providerCode;

}
