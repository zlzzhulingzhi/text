package cn.qbs.wa.teach.course.standard.pojo.course;

import cn.qbs.wa.teach.course.common.entity.WCourse;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 插件-课程表(WCourse)分页查询插件-课程表响应
 *
 * @author makejava
 * @since 2022-03-01 14:25:16
 */
@Data
public class CoursePageByChildResponse extends WCourse {

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程类型 live：直播、record：录播、mix：综合】")
    private String courseType;

    @ApiModelProperty(value = "【课程名称】")
    private String courseName;

    @ApiModelProperty(value = "【课程封面】")
    private String coverUrl;

    @ApiModelProperty(value = "【评分】")
    private BigDecimal score;

    @ApiModelProperty(value = "报名人数")
    private Integer signUpNum;

    @ApiModelProperty(value = "【课时数】")
    private Integer lessonNum;

    @ApiModelProperty(value = "【课程积分】")
    private Integer coursePoints;

    @ApiModelProperty(value = "【课程价格 单位分】")
    private Long coursePrice;

    @ApiModelProperty(value = "【最大报名人数 0-无限制】")
    private Integer signUpLimit;

    @ApiModelProperty(value = "直播开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "直播结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "【课时管理】")
    private Integer courseManage;

    @ApiModelProperty(value = "是否精品课程")
    private Integer gooded;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "【组件ID】")
    private Long componentId;


}

