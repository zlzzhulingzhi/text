package cn.qbs.wa.train.logistics.pojo.employeeinfo;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 职工信息(EmployeeInfo)分页查询参数
 *
 * @author makejava
 * @version 1.0
 * @date 2022-01-21 11:30:04
 */
@Data
public class EmployeeInfoPageRequest extends BasePageRequest {
    
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

