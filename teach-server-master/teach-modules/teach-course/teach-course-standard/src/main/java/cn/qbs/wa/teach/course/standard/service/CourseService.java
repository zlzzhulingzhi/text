package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.course.common.entity.Course;
import cn.qbs.wa.teach.course.common.entity.CourseCategory;
import cn.qbs.wa.teach.course.standard.pojo.course.*;
import cn.qbs.wa.teach.course.standard.pojo.coursedept.CourseDeptBatchAddRequest;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseListSearchDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.CoursePageSearchDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.CoursePageViewDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 【课程】(Course)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:36
 */
public interface CourseService extends IService<Course> {

    /**
     * 新增【课程】
     * @param params
     * @return
     */
    Course add(CourseAddRequest params);

    /**
     * 分页查询【课程】
     * @param params
     * @return
     */
    IPage<CoursePageResponse> page(CoursePageRequest params);

    /**
     * 部门下的课程分页
     * @param params
     * @return
     */
    IPage<CoursePageResponse> pageByDept(CoursePageRequest params);

    /**
     * 标签下的课程分页
     * @param params
     * @return
     */
    IPage<CoursePageResponse> pageByGroup(CoursePageRequest params);

    /**
     * 部门批量新增课程
     * @param params
     * @return
     */
    boolean deptBatchAdd(CourseDeptBatchAddRequest params);

    /**
     * 部门批量移除课程
     * @param params
     * @return
     */
    boolean deptBatchRemove(CourseRemoveDeptRequest params);

    /**
     * 标签批量新增课程
     * @param params
     * @return
     */
    boolean groupBatchAdd(CourseAddGroupRequest params);

    /**
     * 标签批量移除课程
     * @param params
     * @return
     */
    boolean groupBatchRemove(CourseRemoveGroupRequest params);

    /**
     * 分页查询【我的课程】
     * @param params
     * @return
     */
    IPage<CoursePageByLecturerResponse> myCoursePage(CoursePageByLecturerRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    CourseDetailResponse detail(Long id);

    CourseDetailResponse getCourseName(Long id);

    List<CourseDetailResponse> getCourse(CourseListRequest params);
    /**
     * 更新【课程】
     * @param params
     * @return
     */
    boolean update(CourseUpdateRequest params);

    CourseCategory saveCourseCategory(Long courseId, Long categoryId);

    /**
     * 删除【课程】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    /**
     * 逻辑删除【课程】
     * @param idList
     * @return
     */
    boolean deleteLogicByIds(List<Long> idList);

    /**
     * 根据ID复制一门新课程
     * @param courseId 课程ID
     * @param courseName 课程名称
     * @return 课程基础信息
     */
    Course copy(Long courseId, String courseName);

    /**
     * 新增课程章节内容
     * @param params 章节内容
     * @return 课程信息
     */
    Course addContent(CourseContentAddRequest params);

    /**
     * 根据课程ID查询课程章节目录
     * @param courseId 课程ID
     * @return 课程章节目录
     */
    CourseContentResponse detailContent(Long courseId);

    /**
     * 商城内部远程接口
     * */
    CourseContentResponse shopDetailContent(Long courseId);

    /**
     * 修改课程章节内容
     * @param params 章节内容
     * @return 是否成功
     */
    Boolean updateContent(CourseContentUpdateRequest params);

    /**
     * 根据ID复制一门新课程
     * @param courseId 课程ID
     * @param toOrg 机构
     * @return 课程基础信息
     */
    Course copyToOrg(Long courseId, String courseName, Long toOrg);

    /**
     * 课程中心-分页列表
     * @param params 查询参数
     * @return 课程列表
     */
    IPage<CoursePageViewDTO> pageCenter(CoursePageSearchDTO params);

    /**
     * 推荐课程
     */
    List<CoursePageViewDTO> recommend(CourseListSearchDTO params);


    /**
     * 机构首页课程
     */
    IPage<CoursePageViewDTO> pageCourseByIndex(CoursePageIndexRequest params);

    /**
     * 机构首页课程根据子插件id搜索
     */
    IPage<CoursePageByChildResponse> pageByChild(CoursePageRequest params);

    /**
     * 平台首页课程选择器
     */
    IPage<CoursePageResponse> pageAdmin(CoursePageRequest params);

    List<CourseListResponse> listSelect(CourseListRequest params);

    /**
     * 商城获取课程列表
     * */
    List<CourseListResponse> shopListSelect(CourseListRequest params);

    Boolean sortContentTree(CourseContentSortRequest request);

    CourseContentResponse contents(Long courseId);

    Long categoryCount(List<Long> categoryIdList, Long lecturerId, Long studentId);

    Long groupCount(List<Long> groupIdList);

    Long deptCount(List<Long> deptIdList);

    /**
     * 分页查询【课程】
     * @param params
     * @return
     */
    IPage<CoursePageResponse> shopCoursePage(CoursePageRequest params);

    IPage<CoursePageResponse> getOrgList(CoursePageRequest params);
}

