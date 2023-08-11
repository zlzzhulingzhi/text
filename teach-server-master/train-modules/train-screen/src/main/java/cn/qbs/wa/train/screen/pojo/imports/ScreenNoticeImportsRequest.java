package cn.qbs.wa.train.screen.pojo.imports;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ScreenNoticeImportsRequest {

    @ApiModelProperty(value = "通知列表")
    List<ScreenNoticeDataParseResult> screenNoticeDataParseResultList;
}
