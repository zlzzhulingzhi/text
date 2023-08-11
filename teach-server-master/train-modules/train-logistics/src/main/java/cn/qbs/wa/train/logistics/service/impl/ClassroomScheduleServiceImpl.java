package cn.qbs.wa.train.logistics.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.train.logistics.entity.ClassroomSchedule;
import cn.qbs.wa.train.logistics.mapper.ClassroomScheduleMapper;
import cn.qbs.wa.train.logistics.pojo.classroomschedule.*;
import cn.qbs.wa.train.logistics.service.ClassroomScheduleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 教室日程表(ClassroomSchedule)表服务实现类
 *
 * @author makejava
 * @since 2022-10-18 11:23:14
 */
@Slf4j
@Service("classroomScheduleService")
public class ClassroomScheduleServiceImpl
    extends ServiceImpl<ClassroomScheduleMapper, ClassroomSchedule>
    implements ClassroomScheduleService {

  @Override
  public boolean add(ClassroomScheduleAddRequest params) {
    ClassroomSchedule classroomSchedule = new ClassroomSchedule();
    BeanUtils.copyProperties(params, classroomSchedule);
    return this.save(classroomSchedule);
  }

  @Override
  public IPage<ClassroomSchedulePageResponse> page(ClassroomSchedulePageRequest params) {
    return baseMapper.page(params.createMpPage(), params);
  }

  @Override
  public ClassroomScheduleDetailResponse detail(Serializable id) {
    return baseMapper.selectDetailById(id);
  }

  @Override
  public boolean update(ClassroomScheduleUpdateRequest params) {
    if (params.getId() == null) {
      throw new IllegalParamsException("ID不能为空！");
    }
    ClassroomSchedule classroomSchedule = new ClassroomSchedule();
    BeanUtils.copyProperties(params, classroomSchedule);
    return this.updateById(classroomSchedule);
  }

  @Override
  public boolean deleteByIds(List<Long> idList) {
    return this.removeByIds(idList);
  }
}
