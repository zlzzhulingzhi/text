package cn.qbs.wa.teach.exam.admin.pojo.exam;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 阅卷分页查询响应
 * @Author zcm
 * @Date 2022-01-15 11:32
 * @Version 1.0
 */
@Data
public class MarkingPageResponse {

    @ApiModelProperty(value = "考试ID")
    private String examId;

    @ApiModelProperty(value = "考试名称")
    private String examName;

    @ApiModelProperty(value = "提交人数")
    private Integer submitPeopleCount;

    @ApiModelProperty(value = "总份数")
    private Integer totalCopies;

    @ApiModelProperty(value = "已批改数")
    private Integer correctedCopies;

    @JSONField(serialize = false)
    @JsonIgnore
    @ApiModelProperty(value = "未批改数")
    private Integer unCorrectCount;

    @ApiModelProperty(value = "批改状态 1 未完成 2 已完成")
    private Integer correctStatus;

}

