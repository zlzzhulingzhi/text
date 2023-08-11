package cn.qbs.wa.train.logistics.pojo.memberclockin;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * 学员打卡记录(MemberClockIn)分页查询参数
 *
 * @author makejava
 * @since 2022-12-26 15:42:22
 */
@Data
public class MemberClockInCalendarRequest {

    @ApiModelProperty(value = "查询年份")
    private Integer searchYear;

    @ApiModelProperty(value = "查询月份")
    private Integer searchMonth;

    @ApiModelProperty(value = "打卡日期",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate clockInDate;

    @ApiModelProperty(hidden = true)
    private LocalDate searchStartDate;

    @ApiModelProperty(hidden = true)
    private LocalDate searchEndDate;

    @ApiModelProperty(value = "学员用户ID",hidden = true)
    private Long memberId;

}

