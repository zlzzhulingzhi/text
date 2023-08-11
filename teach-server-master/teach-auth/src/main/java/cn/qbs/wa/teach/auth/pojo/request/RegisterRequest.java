package cn.qbs.wa.teach.auth.pojo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户注册对象
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RegisterRequest extends LoginRequest {
    @ApiModelProperty(value = "机构Id")
    Long orgId;
}
