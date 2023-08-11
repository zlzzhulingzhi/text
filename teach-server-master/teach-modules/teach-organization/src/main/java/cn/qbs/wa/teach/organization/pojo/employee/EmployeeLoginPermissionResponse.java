package cn.qbs.wa.teach.organization.pojo.employee;

import cn.qbs.wa.teach.basic.api.pojo.DTO.app.ApplicationFullResultDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/3 14:26
 */
@Data
public class EmployeeLoginPermissionResponse {



    @ApiModelProperty("应用列表")
    List<ApplicationFullResultDTO> appList;

    @ApiModelProperty("PC端logo url")
    private String pcLogoUrl;

    @ApiModelProperty(value = "H5端logo url")
    private String h5LogoUrl;

    @ApiModelProperty("机构名称")
    String orgName;

    @ApiModelProperty("机构id")
    Long orgId;

    @ApiModelProperty("用户名称")
    String realName;

    @ApiModelProperty("角色名称")
    List<String> roles;
}
