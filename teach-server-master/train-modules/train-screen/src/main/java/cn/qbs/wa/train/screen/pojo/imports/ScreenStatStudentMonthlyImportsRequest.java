package cn.qbs.wa.train.screen.pojo.imports;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ScreenStatStudentMonthlyImportsRequest {

    @ApiModelProperty(value = "每月学员数量统计列表")
    List<ScreenStatStudentMonthlyDataParseResult> screenStatStudentMonthlyDataParseResultList;
}
