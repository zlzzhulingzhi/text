package cn.qbs.wa.teach.organization.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StudentExcelData {
    @ApiModelProperty(value = "行号")
    private Integer rowNum;

    @ApiModelProperty(value = "学员姓名")
    @ExcelProperty(value = "*学员姓名")
    private String realName;

    @ApiModelProperty(value = "手机号码")
    @ExcelProperty(value = "*手机号")
    private String phone;

    @ApiModelProperty(value = "性别")
    @ExcelProperty(value = "性别")
    private String sexName;

    @ApiModelProperty(value = "身份证号")
    @ExcelProperty(value = "身份证号")
    private String idNumber;

    @ApiModelProperty(value = "标签")
    @ExcelProperty(value = "学员标签")
    private String groupName;

}
