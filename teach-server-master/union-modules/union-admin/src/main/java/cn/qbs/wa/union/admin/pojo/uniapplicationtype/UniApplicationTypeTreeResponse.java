package cn.qbs.wa.union.admin.pojo.uniapplicationtype;

import cn.qbs.wa.teach.common.core.utils.TreeNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 统一应用类型(UniApplicationType)统一应用类型详情
 *
 * @author makejava
 * @since 2022-07-08 09:03:11
 */
@Data
public class UniApplicationTypeTreeResponse extends TreeNode<UniApplicationTypeTreeResponse> {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "【父ID】")
    private Long parentId;

    @ApiModelProperty(value = "【统一客户端类型码 如admin、org】")
    private String uniClientCode;

    @ApiModelProperty(value = "【应用类型名称】")
    private String appTypeName;

    @ApiModelProperty(value = "【应用类型排序】")
    private Integer sort;

    @ApiModelProperty(value = "【应用类型是否可用】")
    private Integer enable;

}

