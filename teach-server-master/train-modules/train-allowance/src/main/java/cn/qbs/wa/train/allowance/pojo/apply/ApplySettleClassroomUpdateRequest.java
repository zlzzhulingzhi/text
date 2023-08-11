package cn.qbs.wa.train.allowance.pojo.apply;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author qcj
 */
@Data
public class ApplySettleClassroomUpdateRequest {

  @ApiModelProperty(value = "入驻申请ID")
  private Long applySettleId;

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

  @ApiModelProperty(value = "时间段")
  private String usePeriod;
}
