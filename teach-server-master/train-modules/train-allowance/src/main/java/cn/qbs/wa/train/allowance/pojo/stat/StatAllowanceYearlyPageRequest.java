package cn.qbs.wa.train.allowance.pojo.stat;

import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StatAllowanceYearlyPageRequest extends BasePageRequest {
    @ApiModelProperty(value = "开始年份")
    private Integer startYear;

    @ApiModelProperty(value = "结束年份")
    private Integer endYear;
}
