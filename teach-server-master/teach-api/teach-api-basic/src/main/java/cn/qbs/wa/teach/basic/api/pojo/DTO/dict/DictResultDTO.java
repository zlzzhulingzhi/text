package cn.qbs.wa.teach.basic.api.pojo.DTO.dict;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/11 14:34
 */
@Data
public class DictResultDTO {

    @ApiModelProperty(value = "字典码")
    private String code;

    @ApiModelProperty(value = "字典值")
    private String dictKey;

    @ApiModelProperty(value = "字典名称")
    private String dictValue;

    @ApiModelProperty(value = "排序")
    private Integer enabled;

    @ApiModelProperty(value = "排序")
    private Integer sort;
}
