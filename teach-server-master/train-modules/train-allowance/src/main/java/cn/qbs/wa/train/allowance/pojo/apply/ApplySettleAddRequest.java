package cn.qbs.wa.train.allowance.pojo.apply;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @author qcj
 */
@Data
public class ApplySettleAddRequest {

  @ApiModelProperty(value = "机构ID")
  private Long orgId;

  @ApiModelProperty(value = "申请ID")
  private Long applyId;

  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
  @ApiModelProperty(value = "开始日期")
  private LocalDate useDateStart;

  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
  @ApiModelProperty(value = "结束日期")
  private LocalDate useDateEnd;

  @ApiModelProperty(value = "课程ID")
  private Long courseId;

  @ApiModelProperty(value = "课程名称")
  private String courseName;

  @ApiModelProperty(value = "教室备注")
  private String classroomRemark;

  @ApiModelProperty(value = "宿舍备注")
  private String dormitoryRemark;

  @ApiModelProperty(value = "入驻申请明细-教室")
  private List<ApplySettleClassroomAddRequest> applySettleClassroomAddRequests;

  @ApiModelProperty(value = "入驻申请明细-宿舍")
  private List<ApplySettleDormitoryAddRequest> applySettleDormitoryAddRequests;
}
