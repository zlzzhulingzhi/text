package cn.qbs.wa.teach.organization.pojo.employee;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class EmployeeExcelAddRequest {
    @ApiModelProperty(value = "用户名称")
    private String realName;

    @ApiModelProperty(value = "所属部门")
    private String deptName;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    //private String idNumber;
    @ApiModelProperty(value = "性别")
    private String sexName;

    @ApiModelProperty(value = "机构Id")
    private Long orgId;

    @ApiModelProperty(value = "")
    private Long deptId;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "数据校验结果")
    private Boolean passed;

    @ApiModelProperty(value = "错误原因")
    private Set<String> errorReasons;
}
