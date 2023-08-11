package cn.qbs.wa.union.admin.pojo.dict;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DictResponse {

    @ApiModelProperty(value = "字典键")
    private String dictKey;

    @ApiModelProperty(value = "字典值")
    private String dictValue;

    @ApiModelProperty(value = "启禁用")
    private Integer enabled;
}
