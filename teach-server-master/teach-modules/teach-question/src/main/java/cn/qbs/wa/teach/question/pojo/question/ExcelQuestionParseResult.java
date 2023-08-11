package cn.qbs.wa.teach.question.pojo.question;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * Excel试题解析结果类
 * @Author zcm
 * @Date 2021/11/11 14:52
 * @Version 1.0
 */
@Data
public class ExcelQuestionParseResult extends ExcelQuestion {

    @ApiModelProperty(value = "是否校验通过")
    private Boolean passed;

    @ApiModelProperty(value = "错误原因")
    private Set<String> errorReasons;

}
