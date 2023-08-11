package cn.qbs.wa.train.logistics.pojo.classroom;

import cn.qbs.wa.train.logistics.entity.Classroom;
import cn.qbs.wa.train.logistics.pojo.classroomschedule.ClassroomScheduleDetailResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * 教室表(Classroom)分页查询教室表响应
 *
 * @author makejava
 * @since 2022-10-11 17:30:12
 */
@Data
public class ClassroomPageResponse extends Classroom {

  @ApiModelProperty(value = "机构名称")
  private String orgName;

  @ApiModelProperty(value = "使用日期")
  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
  private LocalDate useDate;

  @ApiModelProperty(value = "时间段")
  private String usePeriod;

  @ApiModelProperty(value = "机构ID")
  private Long orgId;

  @ApiModelProperty(value = "教室日程情况")
  List<ClassroomScheduleDetailResponse> classroomScheduleDetailResponses = Collections.emptyList();
}
