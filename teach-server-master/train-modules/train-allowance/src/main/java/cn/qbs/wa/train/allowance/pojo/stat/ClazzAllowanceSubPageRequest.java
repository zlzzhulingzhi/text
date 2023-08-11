package cn.qbs.wa.train.allowance.pojo.stat;

import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClazzAllowanceSubPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "ID")
    private Long id;
}
