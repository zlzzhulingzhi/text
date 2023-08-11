package cn.qbs.wa.teach.exam.api.pojo.DTO;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 考试考生列表查询参数
 * @Author zcm
 * @Date 2022-01-19 13:53
 * @Version 1.0
 */
@Data
public class ExamineeQueryDTO {

    @NotNull(message = "考试ID不能为空！")
    @ApiModelProperty(value = "考试id")
    private Long examId;

    @ApiModelProperty(value = "1 查询通过 2查询未通过")
    private Integer searchStatus;

}

