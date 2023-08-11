package cn.qbs.wa.train.logistics.api;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.logistics.api.factory.RemoteClassroomScheduleFallbackFactory;
import cn.qbs.wa.train.logistics.api.pojo.DTO.ClassroomSchedule.ClassroomScheduleDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author qcj
 */
@FeignClient(
    contextId = "remoteClassroomScheduleService",
    name = "train-logistics",
    path = "/logistics/classroomSchedule",
    fallbackFactory = RemoteClassroomScheduleFallbackFactory.class)
public interface RemoteClassroomScheduleService {

  @PostMapping("saveBatch")
  @ApiOperation("批量插入教室日程表")
  R<Boolean> saveBatch(@RequestBody List<ClassroomScheduleDTO> classroomScheduleDTOS);

  @PostMapping("deleteByApplyId")
  @ApiOperation("删除教室日程表")
  R<Boolean> deleteByApplyId(@RequestBody Long applyId);

  @PostMapping("checkClassroomUse")
  @ApiOperation("确认教室使用情况")
  R<Boolean> checkClassroomUse(@RequestBody List<ClassroomScheduleDTO> classroomScheduleDTOS);
}
