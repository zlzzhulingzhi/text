package cn.qbs.wa.teach.organization.excel;

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
public class EmployeeExcelData {

    // 用户名称 手机号码 性别 基础数据类.这里的排序和excel里面的排序一致
    @ApiModelProperty(value = "行号")
    private Integer rowNum;

    @ApiModelProperty(value = "用户名称")
    @ExcelProperty(value = "*用户姓名")
    private String realName;

    @ApiModelProperty(value = "手机号码")
    @ExcelProperty(value = "*手机号")
    private String phone;

    @ApiModelProperty(value = "性别")
    @ExcelProperty(value = "性别")
    private String sexName;

    @ApiModelProperty(value = "所属部门")
    @ExcelProperty(value = "所属部门")
    private String deptName;

    @ApiModelProperty(value = "所属部门id")
    private Long deptId;

    private Long orgId;

    @ApiModelProperty(value = "【性别 0-未知 1-男 2-女】")
    private Integer sex;
}
