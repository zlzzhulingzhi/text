package cn.qbs.wa.train.logistics.pojo.classroom;

import cn.qbs.wa.train.logistics.entity.Classroom;
import cn.qbs.wa.train.logistics.pojo.classroomschedule.ClassroomScheduleDetailResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 教室表(Classroom)分页查询教室表响应
 *
 * @author makejava
 * @since 2022-10-11 17:30:12
 */
@Data
public class ClassroomPriceResponse{

  @ApiModelProperty(value = "场地租赁费")
  private BigDecimal siteFee;

  @ApiModelProperty(value = "水电费")
  private BigDecimal waterPowerFee;

  @ApiModelProperty(value = "新风费用")
  private BigDecimal airExhaustFee;

  @ApiModelProperty(value = "空调费用")
  private BigDecimal airConditionerFee;

  @ApiModelProperty(value = "场地单价")
  private BigDecimal siteUnitPrice;

  @ApiModelProperty(value = "水电单价")
  private BigDecimal waterPowerUnitPrice;

  @ApiModelProperty(value = "新风单价")
  private BigDecimal airExhaustUnitPrice;

  @ApiModelProperty(value = "空调单价")
  private BigDecimal airConditionerUnitPrice;

  @ApiModelProperty(value = "每日总计费用")
  private BigDecimal totalFee;
}
