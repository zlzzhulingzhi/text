package cn.qbs.wa.train.basic.pojo.role;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/3 10:34
 */
@Data
public class RoleRequest {

    @ApiModelProperty(value = "【角色代码】")
    private String code;

    @ApiModelProperty(value = "【角色代码集合】")
    private List<String> codes;
}
