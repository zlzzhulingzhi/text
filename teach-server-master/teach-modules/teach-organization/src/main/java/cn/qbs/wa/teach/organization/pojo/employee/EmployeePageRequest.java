package cn.qbs.wa.teach.organization.pojo.employee;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

import java.util.List;

/**
 * 职工(Employee)分页查询参数
 *
 * @author makejava
 * @since 2021-11-09 20:11:21
 */
@Data
public class EmployeePageRequest extends BasePageRequest {

    @ApiModelProperty(value = "角色id")
    private Long roleId;

    @ApiModelProperty(value = "组织机构id")
    private Long orgId;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "部门id数组")
    private List<Long> deptIdList;

    @ApiModelProperty(value = "搜索内容")
    private String searchContent;

    @ApiModelProperty("加密搜索内容")
    String encodeSearchContent;

    @ApiModelProperty(value = "用户id数组")
    private List<Long> userIdList;

    @ApiModelProperty(value = "账号启用状态 0-禁用 1-启用")
    private Integer enabled;

}

