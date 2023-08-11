package cn.qbs.wa.teach.course.standard.mapper;

import java.util.List;
import java.io.Serializable;

import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageRequest;
import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.course.common.entity.CourseGroup;
import cn.qbs.wa.teach.course.standard.pojo.coursegroup.CourseGroupDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.coursegroup.CourseGroupPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.coursegroup.CourseGroupPageResponse;

/**
 * 课程标签表(CourseGroup)表数据库访问层
 *
 * @author makejava
 * @since 2022-08-11 15:23:57
 */
public interface CourseGroupMapper extends BaseMapper<CourseGroup> {

	/**
	 * 批量新增数据（MyBatis原生foreach方法）
	 *
	 * @param entities List<CourseGroup> 实例对象列表
	 * @return 影响行数
	 */
	int insertBatch(@Param("entities") List<CourseGroup> entities);

	/**
	 * 批量新增或按主键更新数据（MyBatis原生foreach方法）
	 *
	 * @param entities List<CourseGroup> 实例对象列表
	 * @return 影响行数
	 * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
	 */
	int insertOrUpdateBatch(@Param("entities") List<CourseGroup> entities);

	IPage<CoursePageResponse> pageByGroup(@Param("page") IPage<?> page, @Param("params") CoursePageRequest params);

	IPage<CoursePageResponse> pageByGroupAndLecturer(@Param("page") IPage<?> page, @Param("params") CoursePageRequest params);

	CourseGroupDetailResponse selectDetailById(Serializable id);

	Long groupCount(@Param("groupIdList") List<Long> groupIdList);

}

