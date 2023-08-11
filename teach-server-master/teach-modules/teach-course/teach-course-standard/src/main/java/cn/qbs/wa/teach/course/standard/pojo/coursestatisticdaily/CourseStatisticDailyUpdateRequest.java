package cn.qbs.wa.teach.course.standard.pojo.coursestatisticdaily;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * 【课程学习每日统计】(CourseStatisticDaily)更新【课程学习每日统计】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:39
 */
@Data
public class CourseStatisticDailyUpdateRequest {

    @ApiModelProperty(value = "【主键标识】")
    private Long id;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【统计时间】")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime statisticDate;

    @ApiModelProperty(value = "【课程学习人数】")
    private Integer learningNum;

    @ApiModelProperty(value = "【课程学习时长】")
    private Long learningDuration;

}

