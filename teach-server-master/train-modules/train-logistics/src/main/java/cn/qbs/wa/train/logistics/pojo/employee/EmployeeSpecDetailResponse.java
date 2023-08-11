package cn.qbs.wa.train.logistics.pojo.employee;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 职工(Employee)职工详情
 *
 * @author makejava
 * @since 2021-11-09 20:11:22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EmployeeSpecDetailResponse extends EmployeeDetailResponse {

    @ApiModelProperty(value = "学历")
    private String education;

    @ApiModelProperty(value = "工作")
    private String job;

    @ApiModelProperty(value = "职位")
    private String position;
}

