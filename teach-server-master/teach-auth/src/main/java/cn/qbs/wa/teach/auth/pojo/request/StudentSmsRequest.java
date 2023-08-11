package cn.qbs.wa.teach.auth.pojo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/4 9:01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StudentSmsRequest extends SendSmsRequest {

    /**
     * 机构ID
     */
    @ApiModelProperty(value = "机构ID")
    private Long orgId;
}
