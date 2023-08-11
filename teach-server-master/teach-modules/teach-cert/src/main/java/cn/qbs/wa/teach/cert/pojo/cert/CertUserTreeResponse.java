package cn.qbs.wa.teach.cert.pojo.cert;

import cn.qbs.wa.teach.common.core.utils.TreeNode;
import cn.qbs.wa.teach.organization.api.pojo.DTO.employee.EmployeeDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CertUserTreeResponse extends TreeNode<CertUserTreeResponse> {

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "部门名")
    private String deptName;

    @ApiModelProperty(value = "部门全称")
    private String fullName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "员工列表")
    private List<CertUserResponse> employees;




}
