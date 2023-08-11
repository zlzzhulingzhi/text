package cn.qbs.wa.train.logistics.pojo.classroom;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ClassroomSummaryResponse {

  @ApiModelProperty(value = "教室类别(字典值)")
  private String roomType;

  @ApiModelProperty(value = "总数")
  private Long totalNum;

  @ApiModelProperty(value = "使用数")
  private Long useNum;

  @ApiModelProperty(value = "空闲数")
  private Long freeNum;

}
