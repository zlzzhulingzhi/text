package cn.qbs.wa.union.auth.pojo.uniapplicationtype;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 统一应用类型(UniApplicationType)分页查询参数
 *
 * @author makejava
 * @since 2022-07-08 09:03:10
 */
@Data
public class UniApplicationTypePageRequest extends BasePageRequest {

    @ApiModelProperty(value = "【父ID】")
    private Integer parentId;

    @ApiModelProperty(value = "【统一客户端类型码 如admin、org】")
    private String uniClientCode;

    @ApiModelProperty(value = "【应用类型名称】")
    private String appTypeName;

    @ApiModelProperty(value = "【应用类型排序】")
    private Integer sort;

    @ApiModelProperty(value = "【应用类型是否可用】")
    private Integer enable;

}

