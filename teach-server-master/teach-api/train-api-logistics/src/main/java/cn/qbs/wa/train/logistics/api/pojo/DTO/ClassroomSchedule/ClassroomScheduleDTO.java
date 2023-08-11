package cn.qbs.wa.train.logistics.api.pojo.DTO.ClassroomSchedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * @author QCJ
 */
@Data
@EqualsAndHashCode(of = {"classroomId", "useDate", "usePeriod"})
public class ClassroomScheduleDTO {

  @ApiModelProperty(value = "机构ID")
  private Long orgId;

  @ApiModelProperty(value = "申请ID")
  private Long applyId;

  @ApiModelProperty(value = "教室ID")
  private Long classroomId;

  @ApiModelProperty(value = "使用日期")
  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
  private LocalDate useDate;

  @ApiModelProperty(value = "使用状态(0-未使用，1-已使用)")
  private Integer useState;

  @ApiModelProperty(value = "时间段")
  private String usePeriod;
}
