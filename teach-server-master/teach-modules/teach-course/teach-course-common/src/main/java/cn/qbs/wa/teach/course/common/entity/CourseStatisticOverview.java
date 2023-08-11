package cn.qbs.wa.teach.course.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author makejava
 * @since 2021-11-29 15:33:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseStatisticOverview extends Model {

    private static final long serialVersionUID = 531263683737901400L;

    @ApiModelProperty(value = "【主键标识】")
    private Long id;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【课程评分】= 【课程总评分】/ 【课程评分人数】")
    private BigDecimal score;

    @ApiModelProperty(value = "【课程总评分】")
    private BigDecimal totalScore;

    @ApiModelProperty(value = "【课程评分人数】")
    private Integer evaluatorNum;

    @ApiModelProperty(value = "【报名人数】")
    private Integer signUpNum;

    @ApiModelProperty(value = "【购买人数】")
    private Integer buyerNum;

    @ApiModelProperty(value = "【课时数】")
    private Integer lessonNum;

    @ApiModelProperty(value = "【学习总人数】")
    private Integer studyTotalNum;

    @ApiModelProperty(value = "【浏览量】")
    private Integer views;

    @ApiModelProperty(value = "【总完成人数】")
    private Integer studyCompletedNum;

    @ApiModelProperty(value = "【创建时间】")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "【最后修改时间】")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
