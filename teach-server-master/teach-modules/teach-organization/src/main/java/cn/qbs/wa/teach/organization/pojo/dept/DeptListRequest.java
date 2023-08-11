package cn.qbs.wa.teach.organization.pojo.dept;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/11 14:23
 */
@Data
public class DeptListRequest {

    @ApiModelProperty("机构id")
    Long orgId;

    @ApiModelProperty("0禁用 1启用")
    Integer enabled;

    @ApiModelProperty("部门名称")
    String deptName;
}
