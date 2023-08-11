package cn.qbs.wa.teach.exam.admin.pojo.exam;

import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 考试表(Exam)创建考试表参数
 *
 * @author zcm
 * @since 2021-12-14 13:52:10
 */
@Data
public class ExamAssignUserPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "部门id集")
    private List<Long> deptIdList;

    @ApiModelProperty(value = "标签id集")
    private List<Long> groupIdList;

    @ApiModelProperty(value = "标签id")
    private Long groupId;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "学员姓名")
    private String realName;

    @NotNull(message = "考试Id不能为空！")
    @ApiModelProperty(value = "考试Id")
    private Long examId;


}

