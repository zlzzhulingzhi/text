package cn.qbs.wa.teach.auth.pojo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author yjx
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StudentLoginRequest extends LoginRequest {
    /**
     * 机构ID
     */
    @ApiModelProperty(value = "机构ID")
    private Long orgId;
}
