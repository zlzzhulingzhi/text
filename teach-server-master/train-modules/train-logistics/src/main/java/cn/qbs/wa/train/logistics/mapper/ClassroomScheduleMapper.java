package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.entity.ClassroomSchedule;
import cn.qbs.wa.train.logistics.pojo.classroomschedule.ClassroomScheduleDetailResponse;
import cn.qbs.wa.train.logistics.pojo.classroomschedule.ClassroomSchedulePageRequest;
import cn.qbs.wa.train.logistics.pojo.classroomschedule.ClassroomSchedulePageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 教室日程表(ClassroomSchedule)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-18 11:23:13
 */
public interface ClassroomScheduleMapper extends BaseMapper<ClassroomSchedule> {

  /**
   * 批量新增数据（MyBatis原生foreach方法）
   *
   * @param entities List<ClassroomSchedule> 实例对象列表
   * @return 影响行数
   */
  int insertBatch(@Param("entities") List<ClassroomSchedule> entities);

  /**
   * 批量新增或按主键更新数据（MyBatis原生foreach方法）
   *
   * @param entities List<ClassroomSchedule> 实例对象列表
   * @return 影响行数
   * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
   */
  int insertOrUpdateBatch(@Param("entities") List<ClassroomSchedule> entities);

  IPage<ClassroomSchedulePageResponse> page(
      @Param("page") IPage<?> page, @Param("params") ClassroomSchedulePageRequest params);

  ClassroomScheduleDetailResponse selectDetailById(Serializable id);
}
