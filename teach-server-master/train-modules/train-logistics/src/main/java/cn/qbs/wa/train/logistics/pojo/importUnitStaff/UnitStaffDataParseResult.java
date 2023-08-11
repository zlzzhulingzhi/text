package cn.qbs.wa.train.logistics.pojo.importUnitStaff;

import cn.qbs.wa.train.logistics.excel.UnitStaffExcelData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

@Data
public class UnitStaffDataParseResult extends UnitStaffExcelData {
    @ApiModelProperty(value = "是否校验通过")
    private Boolean passed;

    @ApiModelProperty(value = "错误原因")
    private Set<String> errorReasons;
}
