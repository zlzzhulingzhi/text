package cn.qbs.wa.train.logistics.pojo.importemployee;

import cn.qbs.wa.train.logistics.excel.EmployeeExcelData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

@Data
public class EmployeeDataParseResult extends EmployeeExcelData {
    @ApiModelProperty(value = "是否校验通过")
    private Boolean passed;

    @ApiModelProperty(value = "错误原因")
    private Set<String> errorReasons;
}
