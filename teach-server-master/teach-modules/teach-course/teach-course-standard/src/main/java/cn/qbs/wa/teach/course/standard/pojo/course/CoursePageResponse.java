package cn.qbs.wa.teach.course.standard.pojo.course;

import cn.qbs.wa.teach.course.common.entity.Course;
import cn.qbs.wa.teach.course.common.entity.CourseLecturer;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 【课程】(Course)分页查询【课程】响应
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@Data
public class CoursePageResponse extends Course {

    private static final long serialVersionUID = -5436189856674693205L;

    @ApiModelProperty(value = "创建人")
    private String realName;

    @ApiModelProperty(value = "报名人数")
    private Integer signUpNum;

    @ApiModelProperty(value = "所属机构")
    private String orgName;

    @ApiModelProperty(value = "课程分类名称")
    private String categoryName;

    @ApiModelProperty(value = "讲师列表")
    private List<CourseLecturer> lecturers;

    @ApiModelProperty(value = "【课程价格 单位分】")
    private Long coursePrice;

    @ApiModelProperty(value = "【精品课程 1-精品课程 0-普通课程(默认)】")
    private Integer gooded;

    @ApiModelProperty(value = "【最大报名人数 0-无限制】")
    private Integer signUpLimit;

    @ApiModelProperty(value = "直播开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "直播结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "0未添加 1已添加")
    private Integer added=0;

    @ApiModelProperty(value = "已添加的部门id")
    private String deptIds;

    @ApiModelProperty(value = "已添加的部门id")
    private String groupIds;

    @ApiModelProperty(value = "【可见用户状态 1 所有人可见 2 部分可见】")
    private Integer userVisibleStatus;
}

