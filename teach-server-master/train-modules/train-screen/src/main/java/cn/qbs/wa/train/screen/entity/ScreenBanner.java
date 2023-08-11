package cn.qbs.wa.train.screen.entity;


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
 * @since 2022-10-09 17:04:41
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ScreenBanner extends Model {

    private static final long serialVersionUID = 416604690650822560L;


    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "文件名称")
    private String fileName;
    
    @ApiModelProperty(value = "文件类型(pic-图片 video-视频)")
    private String fileType;

    @ApiModelProperty(value = "文件路径")
    private String fileUrl;

    @ApiModelProperty(value = "停留时长(单位秒，0代表永久停留)")
    private Integer duration;

    @ApiModelProperty(value = "排序号")
    private Integer sort;

    @ApiModelProperty(value = "展示状态(0-不展示 1-展示)")
    private Integer display;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后修改人ID")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "最后修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
