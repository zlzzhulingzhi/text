package cn.qbs.wa.train.screen.pojo.imports;

import cn.qbs.wa.train.screen.excel.ScreenStatStudentMonthlyExcelData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

@Data
public class ScreenStatStudentMonthlyDataParseResult extends ScreenStatStudentMonthlyExcelData {
    @ApiModelProperty(value = "是否校验通过")
    private Boolean passed;

    @ApiModelProperty(value = "错误原因")
    private Set<String> errorReasons;
}
