package cn.qbs.wa.train.logistics.excel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ImportReportResult extends StudentData{

    @ApiModelProperty(value = "导入结果")
    private String passed;

    @ApiModelProperty(value = "错误原因")
    private String failureReason;

}
