package cn.qbs.wa.teach.question.pojo.question;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Excel试题导入结果
 *
 * @Author zcm
 * @Date 2022-06-17 17:29
 * @Version 1.0
 */
@Data
public class ExcelQuestionImportResult extends ExcelQuestion {

    @ApiModelProperty(value = "导入结果")
    @ExcelProperty(value = "导入结果")
    private String importResult;

    @ApiModelProperty(value = "导入失败原因")
    @ExcelProperty(value = "导入失败原因")
    private String failureReason;

}

