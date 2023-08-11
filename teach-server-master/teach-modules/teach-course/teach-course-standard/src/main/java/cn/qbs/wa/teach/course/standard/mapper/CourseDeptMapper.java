package cn.qbs.wa.teach.course.standard.mapper;

import java.util.List;

import java.io.Serializable;

import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageRequest;
import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.course.common.entity.CourseDept;
import cn.qbs.wa.teach.course.standard.pojo.coursedept.CourseDeptDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.coursedept.CourseDeptPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.coursedept.CourseDeptPageResponse;

/**
 * 课程部门表(CourseDept)表数据库访问层
 *
 * @author makejava
 * @since 2022-08-10 19:27:55
 */
public interface CourseDeptMapper extends BaseMapper<CourseDept> {

	/**
	 * 批量新增数据（MyBatis原生foreach方法）
	 *
	 * @param entities List<CourseDept> 实例对象列表
	 * @return 影响行数
	 */
	int insertBatch(@Param("entities") List<CourseDept> entities);

	/**
	 * 批量新增或按主键更新数据（MyBatis原生foreach方法）
	 *
	 * @param entities List<CourseDept> 实例对象列表
	 * @return 影响行数
	 * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
	 */
	int insertOrUpdateBatch(@Param("entities") List<CourseDept> entities);

	Long deptCount(@Param("deptIdList") List<Long> deptIdList);

	IPage<CoursePageResponse> pageByDept(@Param("page") IPage<?> page, @Param("params") CoursePageRequest params);

	IPage<CoursePageResponse> pageByDeptAndLecturer(@Param("page") IPage<?> page, @Param("params") CoursePageRequest params);

	CourseDeptDetailResponse selectDetailById(Serializable id);

}

