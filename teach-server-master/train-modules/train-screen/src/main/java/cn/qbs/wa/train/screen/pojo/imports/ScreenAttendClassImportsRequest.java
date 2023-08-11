package cn.qbs.wa.train.screen.pojo.imports;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ScreenAttendClassImportsRequest {

    @ApiModelProperty(value = "今日培训班级列表")
    List<ScreenAttendClassDataParseResult> screenAttendClassDataParseResultList;
}
