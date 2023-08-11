package cn.qbs.wa.teach.common.core.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 *
 * @Author zcm
 * @Date 2021-12-23 10:01
 * @Version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IdOrgRequest {

    @NotNull(message = "ID不能为空")
    @ApiModelProperty("ID")
    private Long id;

//    @NotNull(message = "机构ID不能为空")
    @ApiModelProperty("机构ID")
    private Long orgId;


}
