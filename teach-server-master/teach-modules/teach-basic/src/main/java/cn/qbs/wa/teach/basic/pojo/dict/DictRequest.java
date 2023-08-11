package cn.qbs.wa.teach.basic.pojo.dict;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/10 16:04
 */
@Data
public class DictRequest {

    @NotBlank(message = "字典不为空")
    @ApiModelProperty(value = "字典码")
    private String code;

    @ApiModelProperty(value = "启用禁用")
    private Integer enabled;
}
