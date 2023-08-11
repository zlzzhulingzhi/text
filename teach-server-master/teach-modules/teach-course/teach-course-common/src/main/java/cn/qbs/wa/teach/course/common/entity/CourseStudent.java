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
public class CourseStudent extends Model {

    private static final long serialVersionUID = 630154806409086556L;

    @ApiModelProperty(value = "【主键标识】")
    private Long id;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【系统用户ID】")
    private Long userId;

    @ApiModelProperty(value = "【职工ID】")
    private Long studentId;

    @ApiModelProperty(value = "【报名时间】")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime signUpTime;

    @ApiModelProperty(value = "【最后一次学习时间】")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lastStudyTime;

    @ApiModelProperty(value = "【开始学课程节数】")
    private Integer learningCount;

    @ApiModelProperty(value = "【已学完课程节数】")
    private Integer learningFinishCount;

    @ApiModelProperty(value = "【学习课程总节数】")
    private Integer learningTotalCount;

    @ApiModelProperty(value = "【课程学习时长】")
    private Long learningDuration;

    @ApiModelProperty(value = "【学习完成率】")
    private Integer learningRate;

    @ApiModelProperty(value = "【学习完成状态 0：未完成， 1：已完成】")
    private Integer learningCompletion;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "【学习完成时间")
    private LocalDateTime learningCompletionTime;

    @ApiModelProperty(value = "【创建人ID】")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "【创建时间】")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}
