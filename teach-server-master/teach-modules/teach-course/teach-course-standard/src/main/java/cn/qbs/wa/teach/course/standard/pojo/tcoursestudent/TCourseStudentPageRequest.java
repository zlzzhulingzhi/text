package cn.qbs.wa.teach.course.standard.pojo.tcoursestudent;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 课程-预报名学员(TCourseStudent)分页查询参数
 *
 * @author makejava
 * @since 2022-10-27 14:45:56
 */
@Data
public class TCourseStudentPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "课程ID")
    private Long courseId;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "会员用户ID(uni_member)")
    private List<Long> memberIdList;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "学员状态(0-待确认 1-已确认)")
    private Integer status;

}

