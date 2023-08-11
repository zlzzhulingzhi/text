package cn.qbs.wa.teach.course.standard.pojo.coursestudent;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 【课程学员】(CourseStudent)分页查询参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:40
 */
@Data
public class CourseStudentPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【系统用户ID】")
    private Long userId;

    @ApiModelProperty(value = "【系统用户ID】")
    private List<Long> userIds;

    @ApiModelProperty(value = "【手机号码】")
    private String phone;

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【学员所属部门id集合】")
    private List<Long> deptIdList;

    @ApiModelProperty(value = "【学员所属标签id集合】")
    private List<Long> groupIdList;

    @ApiModelProperty(value = "【学员ID】")
    private Long studentId;

    @ApiModelProperty(value = "【报名时间】")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime signUpTime;

    @ApiModelProperty(value = "【最后一次学习时间】")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lastStudyTime;

    @ApiModelProperty(value = "【开始学课程节数】")
    private Integer learningCount;

    @ApiModelProperty(value = "【已学完课程节数】")
    private Integer learningFinishCount;

    @ApiModelProperty(value = "【学习课程总节数】")
    private Integer learningTotalCount;

    @ApiModelProperty(value = "【课程学习时长】")
    private Long learningDuration;

}

