package cn.qbs.wa.train.logistics.api.factory;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.logistics.api.RemoteClassroomScheduleService;
import cn.qbs.wa.train.logistics.api.pojo.DTO.ClassroomSchedule.ClassroomScheduleDTO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
public class RemoteClassroomScheduleFallbackFactory
    implements FallbackFactory<RemoteClassroomScheduleService> {
  @Override
  public RemoteClassroomScheduleService create(Throwable cause) {
    return new RemoteClassroomScheduleService() {
      @Override
      public R<Boolean> saveBatch(List<ClassroomScheduleDTO> classroomScheduleDTOS) {
        return null;
      }

      @Override
      public R<Boolean> deleteByApplyId(Long applyId) {
        return null;
      }

      @Override
      public R<Boolean> checkClassroomUse(List<ClassroomScheduleDTO> classroomScheduleDTOS) {
        return null;
      }

    };
  }
}
