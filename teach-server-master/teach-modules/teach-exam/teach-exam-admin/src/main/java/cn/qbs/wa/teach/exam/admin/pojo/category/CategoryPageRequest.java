package cn.qbs.wa.teach.exam.admin.pojo.category;

import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 分类(Category)分页查询参数
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 17:08:47
 */
@Data
public class CategoryPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "题型分类名称")
    private String name;

    @ApiModelProperty(value = "父节点ID")
    private Long parentId;

    @ApiModelProperty(value = "是否启用 【1：启用，0：废弃】")
    private Boolean enabled;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

}

