package cn.qbs.wa.train.allowance.pojo.apply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class QualificationProjectResponse {

    @ApiModelProperty(value = "项目计划主键")
    private Long id;

    @ApiModelProperty(value = "资质申请表ID")
    private Long qualificationId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "总学时")
    private Integer totalLesson;

    @ApiModelProperty(value = "培训规模")
    private String scale;

    @ApiModelProperty(value = "开始日期")
    private LocalDate startDate;

    @ApiModelProperty(value = "结束日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "课程列表")
    private List<QualificationCourseResponse> courseList;

}
