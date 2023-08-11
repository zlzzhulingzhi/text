package cn.qbs.wa.teach.exam.admin.service;

import cn.qbs.wa.teach.exam.common.entity.ExamDept;
import cn.qbs.wa.teach.exam.admin.pojo.examdept.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 考试部门表(ExamDept)表服务接口
 *
 * @author makejava
 * @since 2022-08-12 13:57:06
 */
public interface ExamDeptService extends IService<ExamDept> {

	/**
	 * 新增考试部门表
	 *
	 * @param params
	 * @return
	 */
	boolean add(ExamDeptBatchAddRequest params);

	boolean addExamUserByDept(List<ExamDept> examDeptList);

	/**
	 * 分页查询考试部门表
	 *
	 * @param params
	 * @return
	 */
	IPage<ExamDeptPageResponse> page(ExamDeptPageRequest params);

	List<ExamDeptDetailResponse> listAssignDept(Long examId);

	/**
	 * 获取详细信息
	 *
	 * @param id
	 * @return
	 */
	ExamDeptDetailResponse detail(Serializable id);

	/**
	 * 更新考试部门表
	 *
	 * @param params
	 * @return
	 */
	boolean update(ExamDeptUpdateRequest params);

	/**
	 * 删除考试部门表
	 *
	 * @param idList
	 * @return
	 */
	boolean deleteByIds(List<Long> idList);

}

