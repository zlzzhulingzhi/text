package cn.qbs.wa.train.logistics.pojo.memberclockin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class MemberClockInCalendarDetailOverViewResponse {

    @ApiModelProperty(value = "学习打卡记录")
    private List<MemberClockInCalendarDetailResponse> studyRecordList;

    @ApiModelProperty(value = "休息打卡记录")
    private List<MemberClockInCalendarDetailResponse> restRecordList;


}

