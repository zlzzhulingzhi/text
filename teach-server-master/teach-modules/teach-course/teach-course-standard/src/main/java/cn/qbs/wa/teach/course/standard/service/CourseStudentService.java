package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.CourseStudent;
import cn.qbs.wa.teach.course.standard.pojo.course.CourseDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.coursegroup.CourseGroupAddRequest;
import cn.qbs.wa.teach.course.standard.pojo.coursestudent.*;
import cn.qbs.wa.teach.course.standard.pojo.dto.*;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程学员】(CourseStudent)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:40
 */
public interface CourseStudentService extends IService<CourseStudent> {

    /**
     * 新增【课程学员】
     */
    boolean add(CourseStudentAddRequest params);

    /**
     * 分页查询【课程学员】
     */
    IPage<CourseStudentPageResponse> page(CourseStudentPageRequest params);

    IPage<CourseStudentPageResponse> pageV2(CourseStudentPageRequest params);

    /**
     * 获取详细信息
     */
    CourseStudentDetailResponse detail(Serializable id);

    /**
     * 更新【课程学员】
     */
    boolean update(CourseStudentUpdateRequest params);

    /**
     * 删除【课程学员】
     */
    boolean deleteByIds(List<Long> idList);

    /**
     * 后台手动移除课程学员
     * @param idList
     * @return
     */
    boolean manualRemoveByIds(List<Long> idList);

    /**
     * 产生导出的Excel数据
     * @param request 请求生成Excel的参数
     * @return 表格传输数据
     */
    List<CourseStudentExcelDTO> generateExcelData(CourseStudentPageRequest request);

    /**
     * 检查课程是否存在学员
     * @param courseId
     * @return
     */
    boolean checkCourseStudent(Long courseId);

    IPage<MyCoursePageDTO> pageMyCourse(MyCoursePageSearchDTO params);

    CourseStudent getStudentCourse(Long userId, Long courseId);

    List<CourseStudent> listCourseByUserId(Long userId);

    void dropOut(Long courseId, Long studentId);

    /**
     * 判断学员是否已报名
     * @param courseId 课程ID
     * @param studentId 学员ID
     */
    boolean isSignUp(Long courseId, Long studentId);

    List<CourseLessonSimpleResponseDTO> detail(List<MyCourseDetailRequestDTO> params);

    List<CourseDetailResponse> info(List<MyCourseDetailRequestDTO> params);

    /**
     * 课程通过指定部门或标签批量添加学员
     * */
    boolean addCourseStudent(List<Long> courseIdList, List<StudentDTO> studentDTOList);

    List<CourseStudent> queryStudentByPlat(CourseStudentQuery query);
}

