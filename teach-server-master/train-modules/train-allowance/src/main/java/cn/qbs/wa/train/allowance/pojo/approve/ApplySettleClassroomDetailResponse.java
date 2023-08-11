package cn.qbs.wa.train.allowance.pojo.approve;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ApplySettleClassroomDetailResponse {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "入驻申请ID")
    private Long applySettleId;

    @ApiModelProperty(value = "机构名称(快照)")
    private String orgName;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "申请时间")
    private LocalDate applyDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "开始日期")
    private LocalDate useDateStart;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "结束日期")
    private LocalDate useDateEnd;

    @ApiModelProperty(value = "教室ID")
    private Long classroomId;

    @ApiModelProperty(value = "单元(快照)")
    private String building;

    @ApiModelProperty(value = "楼层(快照)")
    private String floor;

    @ApiModelProperty(value = "房号(快照)")
    private String roomNo;

    @ApiModelProperty(value = "房型(快照)")
    private String roomType;

    @ApiModelProperty(value = "备注")
    private String remark;
}
