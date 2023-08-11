package cn.qbs.wa.teach.basic.pojo.applicationtype;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * (ApplicationType)更新参数
 *
 * @author makejava
 * @since 2021-11-09 19:14:33
 */
@Data
public class ApplicationTypeUpdateRequest {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "【应用类型名称】")
    private String name;

    @ApiModelProperty(value = "【应用类型排序】")
    private Integer sort;

    @ApiModelProperty(value = "【应用类型是否可用】")
    private Integer enabled;

    @ApiModelProperty(value = "父id")
    private Long parentId;

}

