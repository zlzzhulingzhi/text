package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.CourseGroup;
import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageRequest;
import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageResponse;
import cn.qbs.wa.teach.course.standard.pojo.coursegroup.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.List;

/**
 * 课程标签表(CourseGroup)表服务接口
 *
 * @author makejava
 * @since 2022-08-11 15:23:58
 */
public interface CourseGroupService extends IService<CourseGroup> {

	/**
	 * 新增课程标签表
	 *
	 * @param params
	 * @return
	 */
	boolean add(CourseGroupBatchAddRequest params);

	boolean addCourseStudentByGroup(List<CourseGroupAddRequest> courseGroupAddList);

	List<CourseGroupDetailResponse> listAssignGroup(Long courseId);

	Long groupCount(List<Long> groupIdList);

	/**
	 * 分页查询课程标签表
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
	CourseGroupDetailResponse detail(Serializable id);

	/**
	 * 更新课程标签表
	 *
	 * @param params
	 * @return
	 */
	boolean update(CourseGroupUpdateRequest params);

	/**
	 * 删除课程标签表
	 *
	 * @param idList
	 * @return
	 */
	boolean deleteByIds(List<Long> idList, Long groupId);

}

