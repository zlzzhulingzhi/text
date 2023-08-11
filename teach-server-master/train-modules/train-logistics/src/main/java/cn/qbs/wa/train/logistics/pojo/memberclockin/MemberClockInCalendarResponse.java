package cn.qbs.wa.train.logistics.pojo.memberclockin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;


@Data
public class MemberClockInCalendarResponse  {


    @ApiModelProperty(value = "打卡日期")
    private LocalDate clockInDate;


    @ApiModelProperty(value = "是否打卡")
    private Boolean clockIned=false;



}

