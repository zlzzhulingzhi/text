package cn.qbs.wa.train.basic.api.pojo.DTO.role;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/3 10:34
 */
@Data
public class RoleDTO {

    @ApiModelProperty(value = "【角色名称】")
    private String name;

    @ApiModelProperty(value = "【角色代码】")
    private String code;

}