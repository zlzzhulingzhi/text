package cn.qbs.wa.teach.organization.pojo.dept;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/4 17:25
 */
@Data
public class TreeDeptTotalResponse {

    List<TreeDeptResponse> deptList;

    @ApiModelProperty(value = "是否有子 0 否 1是")
    Integer hasChildren;

}
