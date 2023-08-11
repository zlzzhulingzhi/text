package cn.qbs.wa.train.screen.pojo.imports;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ScreenDataOverviewImportsRequest {

    @ApiModelProperty(value = "数据总览列表")
    List<ScreenDataOverviewDataParseResult> screenDataOverviewDataParseResultList;
}
