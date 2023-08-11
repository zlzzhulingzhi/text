package cn.qbs.wa.teach.organization.pojo.employee;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 职工(Employee)创建职工参数
 *
 * @author makejava
 * @since 2021-11-09 20:11:21
 */
@Data
public class EmployeeBatchAddRequest {

//   @ApiModelProperty("职工数组")
//   @NotEmpty(message = "职工数组不能为空")
//   List<EmployeeAddRequest> batchRequest;

   @ApiModelProperty("机构Id")
   Long orgId;

   @ApiModelProperty("项目Id")
   String eventId;

}

