package cn.qbs.wa.train.logistics.pojo.clazzlessonarrange;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * 班级课程安排(ClazzLessonArrange)创建班级课程安排参数
 *
 * @author makejava
 * @since 2023-03-14 09:21:38
 */
@Data
public class ClazzLessonArrangeAddRequest {

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "班级ID")
    private Long clazzId;

    @ApiModelProperty(value = "课程ID")
    private Long lessonId;

    @ApiModelProperty(value = "内容名")
    private String contentName;

    @ApiModelProperty(value = "开课日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "结课日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "教室ID")
    private Long classroomId;

    @ApiModelProperty(value = "讲师ID")
    private Long lecturerId;

    @ApiModelProperty(value = "讲师姓名")
    private String lecturerName;

    @ApiModelProperty(value = "内容描述")
    private String contentDescription;

}

