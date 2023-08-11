package cn.qbs.wa.teach.exam.admin.service;

import cn.qbs.wa.teach.exam.common.entity.ExamGroup;
import cn.qbs.wa.teach.exam.admin.pojo.examgroup.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 考试标签表(ExamGroup)表服务接口
 *
 * @author makejava
 * @since 2022-08-12 17:22:06
 */
public interface ExamGroupService extends IService<ExamGroup> {

	/**
	 * 新增考试标签表
	 *
	 * @param params
	 * @return
	 */
	boolean add(ExamGroupBatchAddRequest params);

	boolean addExamUserByGroup(List<ExamGroup> examGroupList);

	/**
	 * 分页查询考试标签表
	 *
	 * @param params
	 * @return
	 */
	IPage<ExamGroupPageResponse> page(ExamGroupPageRequest params);

	List<ExamGroupDetailResponse> listAssignGroup(Long examId);

	/**
	 * 获取详细信息
	 *
	 * @param id
	 * @return
	 */
	ExamGroupDetailResponse detail(Serializable id);

	/**
	 * 更新考试标签表
	 *
	 * @param params
	 * @return
	 */
	boolean update(ExamGroupUpdateRequest params);

	/**
	 * 删除考试标签表
	 *
	 * @param idList
	 * @return
	 */
	boolean deleteByIds(List<Long> idList);

}

