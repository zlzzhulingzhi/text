package cn.qbs.wa.train.screen.pojo.imports;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ScreenStatDataDynamicImportsRequest {

    @ApiModelProperty(value = "动态数据统计列表")
    List<ScreenStatDataDynamicDataParseResult> screenStatDataDynamicDataParseResultList;

    @ApiModelProperty(value = "模块")
    private String module;
}
