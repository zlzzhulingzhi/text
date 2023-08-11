package cn.qbs.wa.train.screen.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/12 14:38
 */
@Data
public class ScreenStatStudentMonthlyExcelData {

    // 标题 内容 发布时间 基础数据类.这里的排序和excel里面的排序一致
    @ApiModelProperty(value = "行号")
    private Integer rowNum;

    @ApiModelProperty(value = "年份")
    @ExcelProperty(value = "*年份")
    private String year;

    @ApiModelProperty(value = "月份")
    @ExcelProperty(value = "*月份")
    private Integer month;

    @ApiModelProperty(value = "数量")
    @ExcelProperty(value = "*学员人数")
    private Integer num;

}
