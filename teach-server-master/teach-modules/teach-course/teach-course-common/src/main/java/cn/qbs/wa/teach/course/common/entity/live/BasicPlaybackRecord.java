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
 * @since 2021-12-30 13:36:14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BasicPlaybackRecord extends Model {

    private static final long serialVersionUID = -30432309282322578L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "基础直播场次id")
    private Long basicLiveShowId;

    @ApiModelProperty(value = "回放地址")
    private String playbackUrl;

    @ApiModelProperty(value = "点播fileId")
    private String playbackKey;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "流名称")
    private String streamName;

    @ApiModelProperty(value = "回放记录名称")
    private String recordName;

    @ApiModelProperty(value = "录制文件时长")
    private Long duration;

    @ApiModelProperty(value = "启用状态 0-未启用 1-已启用")
    private Integer enabled;

    @ApiModelProperty(value = "处理状态 -1 失败 1 正常 2 剪辑转码 3下载转码")
    private Integer processStatus;

    @ApiModelProperty(value = "记录来源 1 原视频 2剪辑")
    private Integer recordResource;

    @ApiModelProperty(value = "腾讯云任务id")
    private String taskId;

    @ApiModelProperty(value = "下载地址")
    private String downloadUrl;

    @ApiModelProperty(value = "【最后修改人ID】")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "【最后修改时间】")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
