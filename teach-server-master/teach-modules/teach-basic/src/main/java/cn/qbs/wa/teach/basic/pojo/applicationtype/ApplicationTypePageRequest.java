package cn.qbs.wa.teach.basic.pojo.applicationtype;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * (ApplicationType)分页查询参数
 *
 * @author makejava
 * @since 2021-11-09 19:14:28
 */
@Data
public class ApplicationTypePageRequest extends BasePageRequest {

    @ApiModelProperty(value = "【应用类型名称】")
    private String name;

    @ApiModelProperty(value = "【应用类型排序】")
    private Integer sort;

    @ApiModelProperty(value = "【应用类型是否可用】")
    private Integer enabled;

    @ApiModelProperty(value = "父id")
    private Long parentId;

}

