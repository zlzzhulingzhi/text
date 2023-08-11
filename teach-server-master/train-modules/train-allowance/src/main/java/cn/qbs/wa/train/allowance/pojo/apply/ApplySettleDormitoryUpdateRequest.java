package cn.qbs.wa.train.allowance.pojo.apply;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author qcj
 */
@Data
public class ApplySettleDormitoryUpdateRequest {

  @ApiModelProperty(value = "入驻申请ID")
  private Long applySettleId;

  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
  @ApiModelProperty(value = "开始日期")
  private LocalDate useDateStart;

  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
  @ApiModelProperty(value = "结束日期")
  private LocalDate useDateEnd;

  @ApiModelProperty(value = "房型")
  private String roomType;

  @ApiModelProperty(value = "房间数量")
  private Integer roomNum;

  @ApiModelProperty(value = "备注")
  private String remark;
}
