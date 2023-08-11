package cn.qbs.wa.teach.exam.admin.pojo.exam;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 阅卷分页查询参数
 * @Author zcm
 * @Date 2022-01-15 11:32
 * @Version 1.0
 */
@Data
public class MarkingPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "考试id")
    private Long examId;

    @ApiModelProperty(value = "状态 1-未完成 2-已完成")
    private Integer status;

}

