package cn.qbs.wa.train.allowance.pojo.stat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class FeeChartsResponse {

    @ApiModelProperty("年份列表")
    private List<Integer> years;

    @ApiModelProperty("年份对应的数据")
    private FeesResponse itemList;

}
