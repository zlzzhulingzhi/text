package cn.qbs.wa.teach.course.common.entity;


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
 * @since 2022-06-23 18:13:56
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BasicWhiteBoardRecord extends Model {

    private static final long serialVersionUID = -17256215587529560L;


    @ApiModelProperty(value = "")
    private Long id;


    @ApiModelProperty(value = "机构id")
    private Long orgId;


    @ApiModelProperty(value = "房间id")
    private Long roomId;

    @ApiModelProperty(value = "腾讯云任务id")
    private String taskId;

    @ApiModelProperty(value = "流名称")
    private String streamName;

    @ApiModelProperty(value = "进程结果")
    private String processResult;

    @ApiModelProperty(value = "状态 0未直播 1直播中 2直播结束")
    private Integer status;

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

    @ApiModelProperty(value = "基础直播id")
    private Long basicLiveShowId;

}
