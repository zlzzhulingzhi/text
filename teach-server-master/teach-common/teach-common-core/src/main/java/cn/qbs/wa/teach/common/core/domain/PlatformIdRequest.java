package cn.qbs.wa.teach.common.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;

/**
 * @author Administrator
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class PlatformIdRequest extends IdRequest {

    @NotNull(message = "机构ID不能为空")
    private Long orgId;
}
