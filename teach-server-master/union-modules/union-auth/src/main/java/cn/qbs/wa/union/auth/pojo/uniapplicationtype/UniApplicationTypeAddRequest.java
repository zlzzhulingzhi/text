package cn.qbs.wa.union.auth.pojo.uniapplicationtype;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 统一应用类型(UniApplicationType)创建统一应用类型参数
 *
 * @author makejava
 * @since 2022-07-08 09:03:11
 */
@Data
public class UniApplicationTypeAddRequest {

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

