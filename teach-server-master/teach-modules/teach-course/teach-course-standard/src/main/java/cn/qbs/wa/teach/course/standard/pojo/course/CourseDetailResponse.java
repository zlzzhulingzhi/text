package cn.qbs.wa.teach.course.standard.pojo.course;

import cn.qbs.wa.teach.course.common.entity.CourseLecturer;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseUserVisibleDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.dept.DeptDetailResponseDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.groups.GroupsDetailResponseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.teach.course.common.entity.Course;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 【课程】(Course)【课程】详情
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseDetailResponse extends Course {

    private static final long serialVersionUID = -1130984983601678216L;
    @ApiModelProperty(value = "创建人")
    private String realName;

    @ApiModelProperty(value = "课程分类ID列表")
    private Set<Long> categoryIds;

    @ApiModelProperty(value = "课程分类名称列表")
    private Set<String> categoryNames;

    @ApiModelProperty(value = "可见状态")
    private Integer userVisibleStatus;

    @ApiModelProperty(value = "【倍速播放 0禁止 1允许】")
    private Integer playbackSpeed;

    @ApiModelProperty(value = "【拖动进度 0禁止 1允许】")
    private Integer playbackDrag;

    @ApiModelProperty(value = "讲师列表")
    private List<CourseLecturer> lecturers;

    @ApiModelProperty(value = "可见用户表")
    private List<CourseUserVisibleDTO> userVisibleList;

    @ApiModelProperty(value = "可见部门表")
    private List<DeptDetailResponseDTO> deptDetailResponseDTOList;

    @ApiModelProperty(value = "可见标签表")
    private List<GroupsDetailResponseDTO> groupsDetailResponseDTOList;

    @ApiModelProperty(value = "【课程积分】")
    private Integer coursePoints;

    @ApiModelProperty(value = "【课程价格 单位分】")
    private Long coursePrice;

    @ApiModelProperty(value = "【最大报名人数 0-无限制】")
    private Integer signUpLimit;

    @ApiModelProperty(value = "【报名人数】")
    private Integer signUpNum;

    @ApiModelProperty(value = "【课时管理】")
    private Integer courseManage;

    @ApiModelProperty(value = "【内容模式 1-章节模式 2-节模式 3-无章节模式】")
    private Integer courseMode;

    @ApiModelProperty(value = "直播开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "直播结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "直播ID")
    private Long liveId;

    @ApiModelProperty(value = "直播名称")
    private String liveName;

    @ApiModelProperty(value = "0 不能回放 1可以回放")
    private Integer playbacked;

    @ApiModelProperty(value = "播放状态 0未直播 1直播中 2直播结束")
    private Integer playStatus;

    @ApiModelProperty(value = "回放地址")
    List<String> playbackUrls;

    @ApiModelProperty(value = "【精品课程 1-精品课程 0-普通课程(默认)】")
    private Integer gooded;

    @ApiModelProperty(value = "【虚拟报名人数状态 0-关闭 1-开启】")
    private Integer virtualStatus;

    @ApiModelProperty(value = "【虚拟报名人数】")
    private Integer virtualSignUpNum;

    @ApiModelProperty(value = "【报名验证类型 1-无需验证 2-密码验证】")
    private Integer signUpAuthType;

    @ApiModelProperty(value = "【报名验证值】")
    private String signUpAuthValue;
}

