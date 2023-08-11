package cn.qbs.wa.train.screen.pojo.imports;

import cn.qbs.wa.train.screen.excel.ScreenStatDataDynamicExcelData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

@Data
public class ScreenStatDataDynamicDataParseResult extends ScreenStatDataDynamicExcelData {
    @ApiModelProperty(value = "是否校验通过")
    private Boolean passed;

    @ApiModelProperty(value = "错误原因")
    private Set<String> errorReasons;
}
