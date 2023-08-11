package cn.qbs.wa.teach.common.core.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @Author zcm
 * @Date 2021/12/4 13:39
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EnableRequest extends IdListRequest {

    @NotNull
    @ApiModelProperty(value = "0 禁用  1 启用")
    private Integer enabled;

}
