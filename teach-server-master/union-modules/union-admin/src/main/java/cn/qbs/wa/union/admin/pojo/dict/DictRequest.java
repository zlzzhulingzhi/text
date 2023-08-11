package cn.qbs.wa.union.admin.pojo.dict;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DictRequest {

    @NotBlank(message = "字典不为空")
    @ApiModelProperty("字典")
    private String code;

    @ApiModelProperty("启禁用状态")
    private Integer enabled;

}
