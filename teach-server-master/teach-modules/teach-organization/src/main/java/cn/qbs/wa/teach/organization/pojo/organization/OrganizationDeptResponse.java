package cn.qbs.wa.teach.organization.pojo.organization;


import cn.qbs.wa.teach.organization.pojo.dept.TreeDeptResponse;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author makejava
 * @since 2021-11-12 09:18:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrganizationDeptResponse extends Model {

    private static final long serialVersionUID = 281819583908654846L;

    @ApiModelProperty(value = "标识")
    private Long id;

    @ApiModelProperty(value = "机构名称")
    private String name;

    @ApiModelProperty(value = "部门列表")
    List<TreeDeptResponse> deptList;

}
