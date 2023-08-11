package cn.qbs.wa.teach.exam.admin.pojo.exam;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 阅卷-考试记录分页查询参数
 *
 * @Author zcm
 * @Date 2022-01-15 16:23
 * @Version 1.0
 */
@Data
public class MarkingExamRecordPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "考试id")
    private Long examId;

    @ApiModelProperty(value = "状态 1 未批阅 2 已批阅")
    private Integer status;

    @ApiModelProperty(value = "考生姓名")
    private String examineeName;

}

