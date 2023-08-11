package cn.qbs.wa.train.logistics.pojo.classroomschedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class LiteClassroomScheduleRequest {

    @NotNull(message = "教室ID不能为空")
    @ApiModelProperty(value = "教室ID")
    private String classroomId;

    @ApiModelProperty(value = "使用日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate dateStart;

    @ApiModelProperty(value = "使用日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate dateEnd;

}
