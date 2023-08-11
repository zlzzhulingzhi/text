package cn.qbs.wa.train.logistics.pojo.dormitoryschedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LiteDormitoryScheduleResponse {

    @ApiModelProperty(value = "使用日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate useDate;

    @ApiModelProperty(value = "一周的星期几")
    private String dayOfWeek;

    @ApiModelProperty(value = "当前预定机构")
    private String orgName;

    public String getDayOfWeek() {
        if (this.useDate != null) {
            return this.useDate.getDayOfWeek().toString();
        }
        return dayOfWeek;
    }

}
