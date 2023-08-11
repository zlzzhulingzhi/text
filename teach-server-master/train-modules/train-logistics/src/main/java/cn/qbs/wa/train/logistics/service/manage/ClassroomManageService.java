package cn.qbs.wa.train.logistics.service.manage;

import cn.qbs.wa.train.logistics.entity.Classroom;
import cn.qbs.wa.train.logistics.pojo.classroom.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 教室表(Classroom)表服务接口
 *
 * @author makejava
 * @since 2022-10-11 17:30:11
 */
public interface ClassroomManageService extends IService<Classroom> {

  IPage<ClassroomPageResponse> getClassroomState(ClassroomPageRequest params);

  IPage<ClassroomPageResponse> page(ClassroomPageRequest params);

  ClassroomDetailResponse detail(Long id);
}
