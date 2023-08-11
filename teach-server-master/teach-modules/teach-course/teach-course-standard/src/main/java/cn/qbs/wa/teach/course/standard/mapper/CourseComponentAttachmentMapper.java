package cn.qbs.wa.teach.course.standard.mapper;

import cn.qbs.wa.teach.course.common.entity.CourseComponentAttachment;
import cn.qbs.wa.teach.course.standard.pojo.coursecomponentattachment.CourseComponentAttachmentDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.coursecomponentattachment.CourseComponentAttachmentPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.coursecomponentattachment.CourseComponentAttachmentPageResponse;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程讲次内容附件】(CourseComponentAttachment)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:38
 */
public interface CourseComponentAttachmentMapper extends BaseMapper<CourseComponentAttachment> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<CourseComponentAttachment> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<CourseComponentAttachment> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<CourseComponentAttachment> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<CourseComponentAttachment> entities);

    IPage<CourseComponentAttachmentPageResponse> page(@Param("page") IPage<?> page, @Param("params") CourseComponentAttachmentPageRequest params);

    @InterceptorIgnore(tenantLine = "true")
    CourseComponentAttachmentDetailResponse selectDetailById(Serializable id);


}

