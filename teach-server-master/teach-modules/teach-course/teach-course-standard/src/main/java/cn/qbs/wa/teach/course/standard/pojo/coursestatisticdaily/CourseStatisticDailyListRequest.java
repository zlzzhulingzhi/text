package cn.qbs.wa.teach.course.standard.pojo.coursestatisticdaily;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * 【课程学习每日统计】(CourseStatisticDaily)分页查询参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:39
 */
@Data
public class CourseStatisticDailyListRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【统计时间】-开始")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate startStatisticDate;

    @ApiModelProperty(value = "【统计时间】-结束")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate endStatisticDate;

}

