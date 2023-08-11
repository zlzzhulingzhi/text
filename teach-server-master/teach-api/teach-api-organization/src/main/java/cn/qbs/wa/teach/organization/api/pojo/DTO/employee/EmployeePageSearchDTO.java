package cn.qbs.wa.teach.organization.api.pojo.DTO.employee;


import cn.qbs.wa.teach.common.core.domain.BasePageSearchComDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 职工(Employee)分页查询参数
 *
 * @author makejava
 * @since 2021-11-09 20:11:21
 */
@Data
public class EmployeePageSearchDTO extends BasePageSearchComDTO {

    @ApiModelProperty(value = "角色id")
    private Long roleId;

    @ApiModelProperty(value = "组织机构id")
    private Long orgId;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "部门id数组")
    private List<Long> deptIdList;

    @ApiModelProperty(value = "搜索内容")
    private String searchContent;

    @ApiModelProperty("加密搜索内容")
    String encodeSearchContent;

    @ApiModelProperty(value = "用户id数组")
    private List<Long> userIdList;

}

