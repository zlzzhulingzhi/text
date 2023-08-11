package cn.qbs.wa.teach.exam.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.exam.admin.mapper.ExamDeptMapper;
import cn.qbs.wa.teach.exam.admin.service.ExamService;
import cn.qbs.wa.teach.exam.admin.service.ExamUserVisibleService;
import cn.qbs.wa.teach.exam.common.entity.ExamDept;
import cn.qbs.wa.teach.exam.admin.service.ExamDeptService;
import cn.qbs.wa.teach.exam.admin.pojo.examdept.*;
import cn.qbs.wa.teach.exam.common.entity.ExamGroup;
import cn.qbs.wa.teach.organization.api.RemoteOrgBackDeptService;
import cn.qbs.wa.teach.organization.api.RemoteStudentService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.dept.DeptDetailResponseDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.orgbackcoupon.OrgDeptOrRoleResponseDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentSearchDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 考试部门表(ExamDept)表服务实现类
 *
 * @author makejava
 * @since 2022-08-12 13:57:06
 */
@Slf4j
@Service("examDeptService")
public class ExamDeptServiceImpl extends ServiceImpl<ExamDeptMapper, ExamDept> implements ExamDeptService {

	@Resource
	private ExamService examService;

	@Resource
	private RemoteStudentService remoteStudentService;

	@Resource
	private ExamUserVisibleService examUserVisibleService;

	@Resource
	private RemoteOrgBackDeptService remoteOrgBackDeptService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean add(ExamDeptBatchAddRequest params) {
		//考试时间检查
		examService.checkExamTime(params.getExamDeptList().get(0).getExamId());
		List<ExamDept> examDeptList = BeanUtil.copyToList(params.getExamDeptList(), ExamDept.class);
		List<ExamDept> filterExamGroupList = examDeptList.stream().filter(i -> {
			Long count = this.count(Wrappers.<ExamDept>lambdaQuery().eq(ExamDept::getDeptId, i.getDeptId()).eq(ExamDept::getExamId, i.getExamId()));
			return count <= 0;
		}).collect(Collectors.toList());
		this.saveBatch(filterExamGroupList);
		return addExamUserByDept(filterExamGroupList);
	}

	@Override
	public boolean addExamUserByDept(List<ExamDept> examDeptList){
		List<Long> examIdList = examDeptList.stream().map(ExamDept::getExamId).distinct().collect(Collectors.toList());
		List<Long> deptIdList = examDeptList.stream().map(ExamDept::getDeptId).distinct().collect(Collectors.toList());
		//远程查询部门下的学员
		StudentSearchDTO searchDTO = new StudentSearchDTO();
		searchDTO.setOrgId(SecurityContextHolder.getOrgId());
		searchDTO.setDeptIdList(deptIdList);
		R<List<StudentDTO>> studentList = remoteStudentService.list(searchDTO);
		if (!studentList.isOk()){
			throw new ServiceException("远程服务失败！");
		}
		return examUserVisibleService.addUser(examIdList, studentList.getData());
	}

	@Override
	public IPage<ExamDeptPageResponse> page(ExamDeptPageRequest params) {
		return baseMapper.page(params.createMpPage(), params);
	}

	@Override
	public List<ExamDeptDetailResponse> listAssignDept(Long examId) {
		List<ExamDeptDetailResponse> examDeptList = BeanUtil.copyToList(this.list(Wrappers.<ExamDept>lambdaQuery().eq(ExamDept::getExamId, examId)), ExamDeptDetailResponse.class);
		IdListRequest request = new IdListRequest();
		request.setIdList(examDeptList.stream().map(ExamDept::getDeptId).collect(Collectors.toList()));
		List<DeptDetailResponseDTO> deptList = remoteOrgBackDeptService.detailList(request).getData();
		if (CollectionUtils.isNotEmpty(deptList)){
			Map<Long, DeptDetailResponseDTO> deptMap = deptList.stream().collect(Collectors.toMap(DeptDetailResponseDTO::getId, o -> o));
			examDeptList.forEach(i -> i.setDeptName(deptMap.get(i.getDeptId()).getDeptName()));
			examDeptList.forEach(i -> i.setParentId(deptMap.get(i.getDeptId()).getParentId()));
		}
		return examDeptList;
	}

	@Override
	public ExamDeptDetailResponse detail(Serializable id) {
		return baseMapper.selectDetailById(id);
	}

	@Override
	public boolean update(ExamDeptUpdateRequest params) {
		if (params.getId() == null) {
			throw new IllegalParamsException("ID不能为空！");
		}
		ExamDept examDept = new ExamDept();
		BeanUtils.copyProperties(params, examDept);
		return this.updateById(examDept);
	}

	@Override
	public boolean deleteByIds(List<Long> idList) {
		return this.removeByIds(idList);
	}

}

