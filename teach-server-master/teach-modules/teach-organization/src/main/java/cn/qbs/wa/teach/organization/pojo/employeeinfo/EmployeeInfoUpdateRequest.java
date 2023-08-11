package cn.qbs.wa.teach.organization.pojo.employeeinfo;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 职工信息(EmployeeInfo)更新职工信息参数
 *
 * @author makejava
 * @version 1.0
 * @date 2022-01-21 11:30:04
 */
@Data
public class EmployeeInfoUpdateRequest {
    
    @ApiModelProperty(value = "主键")
    private Long id;
    
    @ApiModelProperty(value = "机构ID")
    private Long orgId;
    
    @ApiModelProperty(value = "职工ID")
    private Long employeeId;
    
    @ApiModelProperty(value = "学历")
    private String education;
    
    @ApiModelProperty(value = "工作")
    private String job;
    
    @ApiModelProperty(value = "职位")
    private String position;

}

