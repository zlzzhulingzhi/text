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
 * @since 2021-11-18 16:46:12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseLesson extends Model {

    private static final long serialVersionUID = -22226805725687785L;


    @ApiModelProperty(value = "【主键标识】")
    private Long id;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【课程章节ID】")
    private Long chapterId;

    @ApiModelProperty(value = "【讲次名称】")
    private String lessonName;

    @ApiModelProperty(value = "【讲次简介】")
    private String introduction;

    @ApiModelProperty(value = "【排序】")
    private Integer sort;

    @ApiModelProperty(value = "【删除状态 0 正常 1删除】")
    private Integer deleted;

    @ApiModelProperty(value = "【创建人ID】")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "【创建时间】")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "【最后修改人ID】")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "【最后修改时间】")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
