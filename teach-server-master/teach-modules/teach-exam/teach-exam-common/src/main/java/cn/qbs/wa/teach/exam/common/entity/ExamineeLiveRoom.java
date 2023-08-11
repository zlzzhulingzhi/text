package cn.qbs.wa.teach.exam.common.entity;


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
 * @since 2021-12-29 10:42:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExamineeLiveRoom extends Model {

    private static final long serialVersionUID = -31925071559231477L;


    @ApiModelProperty(value = "")
    private Long id;

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

    @ApiModelProperty(value = "rtmp拉流地址")
    private String pullUdpUrl;

    @ApiModelProperty(value = "m3u8拉流地址")
    private String pullM3u8Url;

    @ApiModelProperty(value = "flv拉流地址")
    private String pullFlvUrl;

    @ApiModelProperty(value = "udp拉流地址")
    private String pullRtmpUrl;

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

    @ApiModelProperty(value = "rtmp推流地址")
    private String pushRtmpUrl;

    @ApiModelProperty(value = "webrtc推流地址")
    private String pushWebrtcUrl;

}
