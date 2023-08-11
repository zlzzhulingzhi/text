package cn.qbs.wa.teach.organization.excel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

@Data
public class ImportReportResult extends StudentData{

    @ApiModelProperty(value = "导入结果")
    private String passed;

    @ApiModelProperty(value = "错误原因")
    private String failureReason;

    @ApiModelProperty(value = "所属部门")
    private String deptName;

}
