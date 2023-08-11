package cn.qbs.wa.teach.question.pojo.question;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Excel试题类
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-10 12:00:29
 */
@Data
public class ExcelQuestion {

    @ApiModelProperty(value = "行号")
    private Integer rowNum;

    @ApiModelProperty(value = "题型名称")
    @ExcelProperty(value = "*题型")
    private String questionTypeName;

    @ApiModelProperty(value = "难度名称")
    @ExcelProperty(value = "难度")
    private String difficultyName;

    @ApiModelProperty(value = "分类名称")
    @ExcelProperty(value = "*分类")
    private String categoryName;

    @ApiModelProperty(value = "题干")
    @ExcelProperty(value = "*题干")
    private String content;

    @ApiModelProperty(value = "正确答案")
    @ExcelProperty(value = "正确答案")
    private String answer;

    @ApiModelProperty(value = "解析")
    @ExcelProperty(value = "解析")
    private String answerDesc;

    @ApiModelProperty(value = "分数")
    @ExcelProperty(value = "分数")
    private BigDecimal score;

    @ApiModelProperty(value = "选项A")
    @ExcelProperty(value = "选项A")
    private String optionA;

    @ApiModelProperty(value = "选项B")
    @ExcelProperty(value = "选项B")
    private String optionB;

    @ApiModelProperty(value = "选项C")
    @ExcelProperty(value = "选项C")
    private String optionC;

    @ApiModelProperty(value = "选项D")
    @ExcelProperty(value = "选项D")
    private String optionD;

    @ApiModelProperty(value = "选项E")
    @ExcelProperty(value = "选项E")
    private String optionE;

    @ApiModelProperty(value = "选项F")
    @ExcelProperty(value = "选项F")
    private String optionF;

    @ApiModelProperty(value = "选项G")
    @ExcelProperty(value = "选项G")
    private String optionG;

    @ApiModelProperty(value = "选项H")
    @ExcelProperty(value = "选项H")
    private String optionH;

    @ApiModelProperty(value = "选项I")
    @ExcelProperty(value = "选项I")
    private String optionI;

    @ApiModelProperty(value = "选项J")
    @ExcelProperty(value = "选项J")
    private String optionJ;

}

