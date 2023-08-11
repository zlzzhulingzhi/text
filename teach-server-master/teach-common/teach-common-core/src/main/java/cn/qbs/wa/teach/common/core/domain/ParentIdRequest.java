package cn.qbs.wa.teach.common.core.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author zcm
 * @Date 2021/12/4 11:35
 * @Version 1.0
 */
@Data
public class ParentIdRequest {

    @NotNull(message = "父ID不能为空")
    @ApiModelProperty(value = "父ID")
    private Long parentId;

}
