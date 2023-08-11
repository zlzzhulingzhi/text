package cn.qbs.wa.train.allowance.pojo.stat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class YearlyPieRequest{
    @ApiModelProperty(value = "年份")
    private Integer year;
}
