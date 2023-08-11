package cn.qbs.wa.train.logistics.pojo.classroom;


import cn.qbs.wa.teach.domain.BasePageRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;


@Data
public class ClassroomSummaryRequest extends BasePageRequest {

    @ApiModelProperty(value = "教室类别(字典值)")
    private String roomType;

    @ApiModelProperty(value = "使用日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate useDate;

    @ApiModelProperty(value = "统计类型")
    private String summaryType;

}

