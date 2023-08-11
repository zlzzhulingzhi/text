package cn.qbs.wa.train.logistics.excel;

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
public class UnitStaffExcelData {

    // 真实姓名 手机号码 性别 基础数据类.这里的排序和excel里面的排序一致
    @ApiModelProperty(value = "行号")
    private Integer rowNum;

    @ApiModelProperty(value = "真实姓名")
    @ExcelProperty(value = "*单位员工姓名")
    private String RealName;

    @ApiModelProperty(value = "手机号码")
    @ExcelProperty(value = "*手机号")
    private String phone;

    @ApiModelProperty(value = "性别")
    @ExcelProperty(value = "性别")
    private String sexName;

    @ApiModelProperty(value = "身份号码")
    @ExcelProperty(value = "身份证号")
    private String idNumber;

    @ApiModelProperty(value = "学历")
    @ExcelProperty(value = "学历")
    private String education;

    private Long unitId;

    private String account;

    @ApiModelProperty(value = "【性别 0-未知 1-男 2-女】")
    private Integer sex;
}
