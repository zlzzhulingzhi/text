package cn.qbs.wa.teach.course.standard.pojo.courseinfo;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 【课程信息】(CourseInfo)创建【课程信息】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:38
 */
@Data
public class CourseInfoAddRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【课程内容】")
    private String courseContent;

    @ApiModelProperty(value = "【课程积分】")
    private Integer coursePoints;

    @ApiModelProperty(value = "【倍速播放 0禁止 1允许】")
    private Integer playbackSpeed;

    @ApiModelProperty(value = "【可见用户状态 1 所有人可见 2 部分可见】")
    private Integer userVisibleStatus;

    @ApiModelProperty(value = "【评论状态 0关闭评论 1开启评论 】")
    private Integer discussStatus;

}

