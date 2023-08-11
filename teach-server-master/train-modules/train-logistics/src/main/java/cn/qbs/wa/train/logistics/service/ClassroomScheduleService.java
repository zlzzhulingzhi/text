package cn.qbs.wa.train.logistics.service;

import cn.qbs.wa.train.logistics.entity.ClassroomSchedule;
import cn.qbs.wa.train.logistics.pojo.classroomschedule.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 教室日程表(ClassroomSchedule)表服务接口
 *
 * @author makejava
 * @since 2022-10-18 11:23:13
 */
public interface ClassroomScheduleService extends IService<ClassroomSchedule> {

  /**
   * 新增教室日程表
   *
   * @param params
   * @return
   */
  boolean add(ClassroomScheduleAddRequest params);

  /**
   * 分页查询教室日程表
   *
   * @param params
   * @return
   */
  IPage<ClassroomSchedulePageResponse> page(ClassroomSchedulePageRequest params);

  /**
   * 获取详细信息
   *
   * @param id
   * @return
   */
  ClassroomScheduleDetailResponse detail(Serializable id);

  /**
   * 更新教室日程表
   *
   * @param params
   * @return
   */
  boolean update(ClassroomScheduleUpdateRequest params);

  /**
   * 删除教室日程表
   *
   * @param idList
   * @return
   */
  boolean deleteByIds(List<Long> idList);
}
