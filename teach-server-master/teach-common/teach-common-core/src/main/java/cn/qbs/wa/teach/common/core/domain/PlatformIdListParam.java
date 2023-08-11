package cn.qbs.wa.teach.common.core.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 *
 * @Author zcm
 * @Date 2022-06-13 16:08
 * @Version 1.0
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PlatformIdListParam extends IdListParam {

    @NotNull(message = "机构ID不能为空")
    @ApiModelProperty("机构ID")
    private Long orgId;

}
