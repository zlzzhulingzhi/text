package cn.qbs.wa.teach.exam.admin.pojo.exam;

import cn.qbs.wa.teach.domain.BasePageRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 考试分页查询参数
 *
 * @Author WX
 * @Date 2022-08-15 13:49
 * @Version 1.0
 */
@Data
public class ExamUserRecordPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "考试id")
    private Long examId;

    @ApiModelProperty(value = "学员id")
    private Long studentId;

    @ApiModelProperty(value = "用户id集合")
    private List<Long> userIdList;

    @ApiModelProperty(value = "考生id")
    private Long examineeId;

    @ApiModelProperty(value = "考试名称")
    private String examName;

    @ApiModelProperty(value = "学员姓名")
    private String realName;

    @ApiModelProperty(value = "部门id集")
    private List<Long> deptIdList;

    @ApiModelProperty(value = "标签id集")
    private List<Long> groupIdList;

    @ApiModelProperty(value = "手机号")
    private String phone;

}

