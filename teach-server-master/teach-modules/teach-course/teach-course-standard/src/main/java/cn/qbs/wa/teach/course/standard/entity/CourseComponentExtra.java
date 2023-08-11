package cn.qbs.wa.teach.course.standard.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author makejava
 * @since 2023-02-08 08:47:57
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseComponentExtra extends Model {

    private static final long serialVersionUID = -13847419851402797L;


    @ApiModelProperty(value = "【主键标识】")
    private Long id;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【讲次ID】")
    private Long lessonId;

    @ApiModelProperty(value = "【组件ID】")
    private Long componentId;

    @ApiModelProperty(value = "开课日期")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "结课日期")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "【讲师ID】")
    private Long lecturerId;

    @ApiModelProperty(value = "【讲师姓名】")
    private String lecturerName;

    @ApiModelProperty(value = "【资源名】")
    private String extraName;

    @ApiModelProperty(value = "【资源描述】")
    private String extraDescription;

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
