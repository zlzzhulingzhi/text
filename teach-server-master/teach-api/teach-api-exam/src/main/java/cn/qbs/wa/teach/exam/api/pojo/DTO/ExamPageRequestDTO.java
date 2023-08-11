package cn.qbs.wa.teach.exam.api.pojo.DTO;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 考试分页查询参数
 * @Author zcm
 * @Date 2022-01-19 13:53
 * @Version 1.0
 */
@Data
public class ExamPageRequestDTO {

    @ApiModelProperty(value = "当前页")
    private Integer current;

    @ApiModelProperty(value = "页码大小")
    private Integer size;

    @ApiModelProperty(value = "考试名称")
    private String examName;

}

