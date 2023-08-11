package cn.qbs.wa.teach.course.standard.pojo.course;


import cn.qbs.wa.teach.course.common.entity.CourseLecturer;
import cn.qbs.wa.teach.course.common.entity.CourseUserDeptVisible;
import cn.qbs.wa.teach.course.common.entity.CourseUserGroupVisible;
import cn.qbs.wa.teach.course.common.entity.CourseUserVisible;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 【课程】(Course)更新【课程】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@Data
public class CourseUpdateRequest {

    @NotNull(message = "ID不能为空")
    @ApiModelProperty(value = "【主键标识】")
    private Long id;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @NotBlank(message = "课程名称不能为空")
    @ApiModelProperty(value = "【课程名称】")
    private String courseName;

    @ApiModelProperty(value = "【课程封面】")
    @NotBlank(message = "课程封面不能为空")
    private String coverUrl;

    @ApiModelProperty(value = "【课程简介】")
    @Length(max = 5000, message = "专题简介长度不能超过5000字")
    private String introduction;

    @ApiModelProperty(value = "【课程内容】")
    private String courseContent;

    @NotEmpty(message = "请选择课程分类")
    @ApiModelProperty(value = "【课程分类】")
    private Set<Long> categoryIds;

    @ApiModelProperty(value = "【上架状态 0下架 1上架】")
    private Integer shelfStatus;

    @NotEmpty(message = "讲师不能为空")
    @ApiModelProperty(value = "【讲师列表】", notes = "讲师的ID、名称、头像、简介都要传递")
    private List<CourseLecturer> lecturers;

    @NotNull(message = "请选择可见用户")
    @ApiModelProperty(value = "【可见用户状态 1 所有人可见 2 部分可见】")
    private Integer userVisibleStatus;

    @ApiModelProperty(value = "【可见用户列表】", notes = "讲师的ID、名称、头像、简介都要传递")
    private List<CourseUserVisible> visibleUsers;

    @ApiModelProperty(value = "【可见分组】")
    private List<CourseUserGroupVisible> courseUserGroupVisibles;

    @ApiModelProperty(value = "【可见部门】")
    private List<CourseUserDeptVisible> courseUserDeptVisibles;

    @ApiModelProperty(value = "【课程价格 单位分】")
    private Long coursePrice;

    @ApiModelProperty(value = "【最大报名人数 0-无限制】")
    private Integer signUpLimit;

    @ApiModelProperty(value = "【课程积分】")
    private Integer coursePoints;

    @ApiModelProperty(value = "直播开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "直播结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "【倍速播放 0禁止 1允许】")
    private Integer playbackSpeed;

    @ApiModelProperty(value = "【拖动进度 0禁止 1允许】")
    private Integer playbackDrag;

    @ApiModelProperty(value = "【内容模式 1-章节模式 2-节模式 3-无章节模式】")
    private Integer courseMode;

    @ApiModelProperty(value = "【虚拟报名人数状态 0-关闭 1-开启】")
    private Integer virtualStatus;

    @ApiModelProperty(value = "【虚拟报名人数】")
    private Integer virtualSignUpNum;

    @ApiModelProperty(value = "【报名验证类型 1-无需验证 2-密码验证】")
    private Integer signUpAuthType;

    @ApiModelProperty(value = "【报名验证值】")
    private String signUpAuthValue;

    @ApiModelProperty(value = "计划标识(0-默认 1-万人计划)")
    private Integer plan;
}

