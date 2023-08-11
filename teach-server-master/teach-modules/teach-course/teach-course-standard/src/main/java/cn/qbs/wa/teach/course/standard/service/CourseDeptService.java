package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.CourseDept;
import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageRequest;
import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageResponse;
import cn.qbs.wa.teach.course.standard.pojo.coursedept.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 课程部门表(CourseDept)表服务接口
 *
 * @author makejava
 * @since 2022-08-10 19:27:55
 */
public interface CourseDeptService extends IService<CourseDept> {

	/**
	 * 新增课程部门表
	 *
	 * @param params
	 * @return
	 */
	boolean add(CourseDeptBatchAddRequest params);

	boolean addCourseStudentByDept(List<CourseDept> courseDeptList, Long orgId);

	List<CourseDeptDetailResponse> listAssignDept(Long courseId);

	Long deptCount(List<Long> deptIdList);

	/**
	 * 分页查询课程部门表
	 *
	 * @param params
	 * @return
	 */
	IPage<CoursePageResponse> page(CoursePageRequest params);

	/**
	 * 获取详细信息
	 *
	 * @param id
	 * @return
	 */
	CourseDeptDetailResponse detail(Serializable id);

	/**
	 * 更新课程部门表
	 *
	 * @param params
	 * @return
	 */
	boolean update(CourseDeptUpdateRequest params);

	/**
	 * 删除课程部门表
	 *
	 * @param idList
	 * @return
	 */
	boolean deleteByIds(List<Long> idList, Long deptId);

}

