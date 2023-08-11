package cn.qbs.wa.teach.exam.admin.pojo.api;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 考试分页查询参数
 * @Author zcm
 * @Date 2022-01-19 13:53
 * @Version 1.0
 */
@Data
public class ApiExamPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "考试名称")
    private String examName;

}

