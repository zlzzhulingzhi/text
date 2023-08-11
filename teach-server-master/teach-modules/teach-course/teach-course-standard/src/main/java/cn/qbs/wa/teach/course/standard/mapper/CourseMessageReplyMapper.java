package cn.qbs.wa.teach.course.standard.mapper;

import java.util.List;

import java.io.Serializable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.course.common.entity.CourseMessageReply;
import cn.qbs.wa.teach.course.standard.pojo.coursemessagereply.CourseMessageReplyDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.coursemessagereply.CourseMessageReplyPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.coursemessagereply.CourseMessageReplyPageResponse;

/**
 * 【课程留言回复记录】(CourseMessageReply)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:39
 */
public interface CourseMessageReplyMapper extends BaseMapper<CourseMessageReply> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<CourseMessageReply> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<CourseMessageReply> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<CourseMessageReply> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<CourseMessageReply> entities);

    IPage<CourseMessageReplyPageResponse> page(@Param("page") IPage<?> page, @Param("params") CourseMessageReplyPageRequest params);

    CourseMessageReplyDetailResponse selectDetailById(Serializable id);

}

