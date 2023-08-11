package cn.qbs.wa.train.logistics.service;

import cn.qbs.wa.train.logistics.entity.ClazzLessonArrange;
import cn.qbs.wa.train.logistics.pojo.clazz.IntegrateClazzResponse;
import cn.qbs.wa.train.logistics.pojo.clazzlessonarrange.*;
import cn.qbs.wa.train.logistics.pojo.lecturer.LecturerClazzMap;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.util.List;

/**
 * 班级课程安排(ClazzLessonArrange)表服务接口
 *
 * @author makejava
 * @since 2023-03-14 09:21:37
 */
public interface ClazzLessonArrangeService extends IService<ClazzLessonArrange> {

    /**
     * 新增班级课程安排
     *
     * @param params
     * @return
     */
    boolean add(ClazzLessonArrangeAddRequest params);

    /**
     * 分页查询班级课程安排
     *
     * @param params
     * @return
     */
    IPage<ClazzLessonArrangePageResponse> page(ClazzLessonArrangePageRequest params);

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    ClazzLessonArrangeDetailResponse detail(Serializable id);

    /**
     * 更新班级课程安排
     *
     * @param params
     * @return
     */
    boolean update(ClazzLessonArrangeUpdateRequest params);

    boolean sort(ClazzLessonArrangeUpdateRequest params);

    List<ClazzLessonArrangeListResponse> listV2(ClazzLessonArrangePageRequest request);

    List<ClazzLessonArrangeListResponse> listV3(ClazzLessonArrangePageRequest request);

    ClazzLessonArrangeWeekResponse getWeek(ClazzLessonArrangePageRequest request);

    List<ClazzLessonArrangeListResponse> listV4(ClazzLessonArrangePageRequest request);

    /**
     * 删除班级课程安排
     *
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    boolean addBatch(List<ClazzLessonArrangeAddRequest> params);

    List<LecturerClazzMap> queryClazzLastByLecturerIds(List<Long> lecturerIds);

    List<IntegrateClazzResponse> listClazzByLecturerId(Long lecturerId);
}

