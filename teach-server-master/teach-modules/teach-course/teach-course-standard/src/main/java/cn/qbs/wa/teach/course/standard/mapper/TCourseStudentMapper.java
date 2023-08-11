package cn.qbs.wa.teach.course.standard.mapper;

import cn.qbs.wa.teach.course.standard.entity.TCourseStudent;
import cn.qbs.wa.teach.course.standard.pojo.tcoursestudent.TCourseStudentPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.tcoursestudent.TCourseStudentPageResponse;
import cn.qbs.wa.teach.course.standard.pojo.tcoursestudent.TCourseStudentResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 课程-预报名学员(TCourseStudent)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-25 10:34:24
 */
public interface TCourseStudentMapper extends BaseMapper<TCourseStudent> {

    IPage<TCourseStudentPageResponse> pageCourseStudent(@Param("page") IPage<?> page, @Param("params") TCourseStudentPageRequest params);

    IPage<TCourseStudentResponse> litePagePreStudent(Page<?> page, @Param("params") TCourseStudentPageRequest params);
}

