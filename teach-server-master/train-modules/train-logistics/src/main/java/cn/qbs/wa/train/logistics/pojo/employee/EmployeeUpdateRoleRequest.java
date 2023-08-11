package cn.qbs.wa.train.logistics.pojo.employee;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 职工(Employee)更新职工角色参数
 *
 * @author makejava
 * @since 2021-11-09 20:11:22
 */
@Data
public class EmployeeUpdateRoleRequest {

    @ApiModelProperty(value = "职工Id")
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "角色id数组")
    @NotEmpty(message = "角色不能为空")
    private List<Long> roleIdList;



}

