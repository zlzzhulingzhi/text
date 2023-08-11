package cn.qbs.wa.teach.course.standard.pojo.courseinfo;

import cn.qbs.wa.teach.course.common.entity.CourseInfo;
import cn.qbs.wa.teach.course.standard.pojo.course.CourseContentResponse;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseCategoryDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseLecturerDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 【课程信息】(CourseInfo)【课程信息】详情
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:38
 */
@Data
public class CourseInfoDetailResponse extends CourseInfo {

    private static final long serialVersionUID = -7101863746051388941L;
    @ApiModelProperty(value = "【课程名称】")
    private String courseName;

    @ApiModelProperty(value = "【课程分类列表】")
    private List<CourseCategoryDTO> categories;

    @ApiModelProperty(value = "【课程分类名】")
    private String categoryName;

    @ApiModelProperty(value = "【课程封面】")
    private String coverUrl;

    @ApiModelProperty(value = "【课程类型 live：直播、record：录播、mix：综合】")
    private String courseType;

    @ApiModelProperty(value = "【课程简介】")
    private String introduction;

    @ApiModelProperty(value = "上架状态 0 下架 1上架")
    private Integer shelfStatus;

    @ApiModelProperty(value = "讲师列表")
    private List<CourseLecturerDTO> lecturers;

    @ApiModelProperty(value = "内容目录")
    private CourseContentResponse content;

    @ApiModelProperty(value = "【课程评分】")
    private BigDecimal score;

    @ApiModelProperty(value = "【报名人数】")
    private Integer signUpNum;

    @ApiModelProperty(value = "【课时数】")
    private Integer lessonNum;

    @ApiModelProperty(value = "【学习总人数】")
    private Integer studyTotalNum;

    @ApiModelProperty(value = "【浏览量】")
    private Integer views;

    @ApiModelProperty(value = "课程学习总进度")
    private Integer learningRate;
}

