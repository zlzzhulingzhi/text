package cn.qbs.wa.teach.course.standard.pojo.tcoursestudent;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 课程-预报名学员(TCourseStudent)分页查询课程-预报名学员响应
 *
 * @author makejava
 * @since 2022-10-27 14:46:00
 */
@Data
public class TCourseStudentResponse {

    @ApiModelProperty(value = "会员用户ID(uni_member)")
    private Long memberId;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "学员状态(0-待确认 1-已确认)")
    private Integer status;

    @ApiModelProperty(value = "报名时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}

