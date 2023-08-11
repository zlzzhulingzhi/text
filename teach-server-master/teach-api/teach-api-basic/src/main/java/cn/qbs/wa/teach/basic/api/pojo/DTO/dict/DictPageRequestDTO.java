package cn.qbs.wa.teach.basic.api.pojo.DTO.dict;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 字典表(Dict)分页查询参数
 *
 * @author makejava
 * @since 2021-11-08 13:32:05
 */
@Data
public class DictPageRequestDTO {

    @ApiModelProperty(value = "字典码")
    private String code;

    @ApiModelProperty(value = "字典值")
    private String dictKey;

    @ApiModelProperty(value = "字典名称")
    private String dictValue;

}

