package cn.qbs.wa.teach.common.core.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/10 8:29
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IdRequest {

    @NotNull(message = "ID不能为空")
    @ApiModelProperty(value = "id")
    Long id;
}
