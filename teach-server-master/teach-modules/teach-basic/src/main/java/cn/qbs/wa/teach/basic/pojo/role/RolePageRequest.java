package cn.qbs.wa.teach.basic.pojo.role;

import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/4 18:52
 */
@Data
public class RolePageRequest extends BasePageRequest {


    @ApiModelProperty(value = "【角色名称】")
    private String name;


    @ApiModelProperty(value = "【启用状态 false-0-未启用 true-1-启用】")
    private Integer enabled;



}
