package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.CourseInfo;
import cn.qbs.wa.teach.course.standard.pojo.courseinfo.*;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseInfoDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 【课程信息】(CourseInfo)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:38
 */
public interface CourseInfoService extends IService<CourseInfo> {

    /**
     * 新增【课程信息】
     * @param params
     * @return
     */
    boolean add(CourseInfoAddRequest params);

    /**
     * 分页查询【课程信息】
     * @param params
     * @return
     */
    IPage<CourseInfoPageResponse> page(CourseInfoPageRequest params);

    /**
     * 获取详细信息
     * @param courseId
     * @return
     */
    CourseInfoDetailResponse detail(Long courseId);

    /**
     * 更新【课程信息】
     * @param params
     * @return
     */
    boolean update(CourseInfoUpdateRequest params);

    /**
     * 删除【课程信息】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    /**
     * 根据课程ID 获取课程其他信息
     * @param courseId 课程ID
     * @return 课程其他信息
     */
    CourseInfo getByCourseId(Long courseId);

    /**
     * 根据课程ID 获取课程其他信息
     * @param courseId 课程ID
     * @return 课程其他信息
     */
    CourseInfoDTO info(Long courseId);

    /**
     * 根据课程ID 获取课程其他信息
     * @param courseId 课程ID
     * @return 课程其他信息
     */
    CourseInfoDTO infos(Long courseId);

    /**
     * 更新课程总时长
     * @param courseId 课程id
     * @param courseDuration 课程时长
     */
    void updateDurationByCourseId(Long courseId, Long courseDuration);

    void updateByCourseId(CourseInfo courseInfo);

    Boolean settingMode(CourseModeSettingRequest params);

    CourseInfoDTO baseInfo(Long courseId);

    boolean signAuth(SiginAuthRequest params);
}

