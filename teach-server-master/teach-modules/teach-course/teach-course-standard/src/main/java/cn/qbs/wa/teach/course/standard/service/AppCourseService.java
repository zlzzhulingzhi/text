package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.standard.pojo.dto.app.ListCourseDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.app.PageCommentDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.app.PageCourseDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.*;
import cn.qbs.wa.teach.course.standard.pojo.dto.app.PageCourseVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * App端课程业务相关接口
 * @author yjx
 */
public interface AppCourseService {
    /**
     * 课程中心-分页列表
     * @param params 查询参数
     * @return 课程列表
     */
    IPage<PageCourseVO> pageCourse(PageCourseDTO params);

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
     * 根据课程ID查询课程章节目录
     * @param courseId 课程ID
     * @return 课程章节目录
     */
    List<CourseChapterDTO> contents(Long courseId);

    /**
     * 课程讲义
     * @param courseId 课程ID
     * @return 讲义列表
     */
    List<CourseAttachmentDTO> attachment(Long courseId);

    /**
     * 推荐课程
     */
    List<PageCourseVO> recommend(ListCourseDTO params);

    /**
     * 分页查询【课程评价】
     * @param params 课程ID
     * @return 评价列表
     */
    IPage<CourseCommentViewDTO> pageComment(PageCommentDTO params);

    CourseInfoDTO infoByAnon(Long courseId);

    CourseInfoDTO anonInfo(Long courseId);

    List<CourseChapterDTO> contentsByAnon(Long courseId);

    /**
     * 商城远程接口
     * */
    List<CourseChapterDTO> shopContentsByAnon(Long courseId);

    List<CourseAttachmentDTO> attachmentByAnon(Long courseId);
}
