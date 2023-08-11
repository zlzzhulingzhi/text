package cn.qbs.wa.teach.exam.admin.pojo.exam;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 考试分页查询参数
 *
 * @Author zcm
 * @Date 2022-05-20 13:49
 * @Version 1.0
 */
@Data
public class ExamPageRequestV2 extends BasePageRequest {

//    @ApiModelProperty(value = "机构id")
//    private Long orgId;

//    @ApiModelProperty(value = "考试id")
//    private Long examId;

    @ApiModelProperty(value = "考试名称")
    private String examName;

    @ApiModelProperty(value = "发布状态 0-未发布 1-已发布")
    private Integer releaseStatus;

    @ApiModelProperty(value = "状态  1 未考试 2 考试中 3考试结束")
    private Integer status;

}

