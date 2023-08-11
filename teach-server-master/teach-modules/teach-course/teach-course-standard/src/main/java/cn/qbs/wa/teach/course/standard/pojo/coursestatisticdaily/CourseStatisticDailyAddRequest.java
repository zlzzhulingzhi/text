package cn.qbs.wa.teach.course.standard.pojo.coursestatisticdaily;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 【课程学习每日统计】(CourseStatisticDaily)创建【课程学习每日统计】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:39
 */
@Data
public class CourseStatisticDailyAddRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @NotNull(message = "课程ID不能为空")
    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【统计时间】")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate statisticDate;

    @ApiModelProperty(value = "【课程学习人数】")
    private Integer learningNum;

    @ApiModelProperty(value = "【课程学习时长】")
    private Long learningDuration;

}

