package cn.qbs.wa.train.allowance.pojo.stat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClazzAllowanceStuResponse extends SummaryStudentResponse {

    @ApiModelProperty(value = "主键")
    private Long id;

}
