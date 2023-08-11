package cn.qbs.wa.train.basic.pojo.dict;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 字典表(Dict)分页查询参数
 *
 * @author makejava
 * @since 2021-11-08 13:32:05
 */
@Data
public class DictPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "父主键")
    private Long parentId;

    @ApiModelProperty(value = "字典码")
    private String code;

    @ApiModelProperty(value = "字典值")
    private String dictKey;

    @ApiModelProperty(value = "字典名称")
    private String dictValue;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "字典备注")
    private String remark;

    @ApiModelProperty(value = "启用禁用")
    private Integer enabled;

}

