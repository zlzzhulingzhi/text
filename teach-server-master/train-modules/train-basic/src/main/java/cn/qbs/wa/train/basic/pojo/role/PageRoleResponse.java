package cn.qbs.wa.train.basic.pojo.role;

import cn.qbs.wa.train.basic.entity.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/4 18:52
 */
@Data
public class PageRoleResponse {

    @ApiModelProperty(value = "总页数")
    private Integer total;

    private List<Role> roleList;

}
