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
 * @since 2022-06-15 10:07:55
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BasicLiveAttachment extends Model {

    private static final long serialVersionUID = 203319811185260550L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "基础直播id")
    private Long basicLiveId;

    @ApiModelProperty(value = "上传文件ID")
    private Long resourceFileId;

    @ApiModelProperty(value = "文件路径")
    private String resourceFilePath;

    @ApiModelProperty(value = "文件名")
    private String resourceFileName;

    @ApiModelProperty(value = "文件类型")
    private String resourceFileType;

    @ApiModelProperty(value = "文件大小")
    private Long resourceFileSize;

    @ApiModelProperty(value = "文件时长")
    private Integer resourceFileDuration;

    @ApiModelProperty(value = "删除状态 0 正常 1删除")
    private Integer deleted;

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

    @ApiModelProperty(value = "转码结果")
    private String transcodeResult;

    @ApiModelProperty(value = "腾讯云任务id")
    private String taskId;

}
