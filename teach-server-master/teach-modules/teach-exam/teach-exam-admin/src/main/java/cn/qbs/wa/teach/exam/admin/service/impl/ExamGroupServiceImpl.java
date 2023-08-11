package cn.qbs.wa.teach.exam.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.exam.admin.mapper.ExamGroupMapper;
import cn.qbs.wa.teach.exam.admin.service.ExamService;
import cn.qbs.wa.teach.exam.admin.service.ExamUserVisibleService;
import cn.qbs.wa.teach.exam.common.entity.ExamGroup;
import cn.qbs.wa.teach.exam.admin.service.ExamGroupService;
import cn.qbs.wa.teach.exam.admin.pojo.examgroup.*;
import cn.qbs.wa.teach.organization.api.RemoteGroupsService;
import cn.qbs.wa.teach.organization.api.RemoteStudentService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.groups.GroupsDetailResponseDTO;
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
 * 考试标签表(ExamGroup)表服务实现类
 *
 * @author makejava
 * @since 2022-08-12 17:22:06
 */
@Slf4j
@Service("examGroupService")
public class ExamGroupServiceImpl extends ServiceImpl<ExamGroupMapper, ExamGroup> implements ExamGroupService {

	@Resource
	private ExamService examService;

	@Resource
	private RemoteStudentService remoteStudentService;

	@Resource
	private ExamUserVisibleService examUserVisibleService;

	@Resource
	private RemoteGroupsService remoteGroupsService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean add(ExamGroupBatchAddRequest params) {
		//考试时间检查
		examService.checkExamTime(params.getExamGroupList().get(0).getExamId());
		List<ExamGroup> examGroupList = BeanUtil.copyToList(params.getExamGroupList(), ExamGroup.class);
		List<ExamGroup> filterExamGroupList = examGroupList.stream().filter(i -> {
			Long count = this.count(Wrappers.<ExamGroup>lambdaQuery().eq(ExamGroup::getGroupId, i.getGroupId()).eq(ExamGroup::getExamId, i.getExamId()));
			return count <= 0;
		}).collect(Collectors.toList());
		this.saveBatch(filterExamGroupList);
		return addExamUserByGroup(examGroupList);
	}

	@Override
	public boolean addExamUserByGroup(List<ExamGroup> examGroupList){
		List<Long> examIdList = examGroupList.stream().map(ExamGroup::getExamId).distinct().collect(Collectors.toList());
		List<Long> groupIdList = examGroupList.stream().map(ExamGroup::getGroupId).distinct().collect(Collectors.toList());
		//远程查询标签下的学员
		StudentSearchDTO searchDTO = new StudentSearchDTO();
		searchDTO.setOrgId(SecurityContextHolder.getOrgId());
		searchDTO.setGroupIdList(groupIdList);
		R<List<StudentDTO>> studentList = remoteStudentService.list(searchDTO);
		if (!studentList.isOk()){
			throw new ServiceException("远程服务失败！");
		}
		return examUserVisibleService.addUser(examIdList, studentList.getData());
	}

	@Override
	public IPage<ExamGroupPageResponse> page(ExamGroupPageRequest params) {
		return baseMapper.page(params.createMpPage(), params);
	}

	@Override
	public List<ExamGroupDetailResponse> listAssignGroup(Long examId) {
		List<ExamGroupDetailResponse> courseGroupList = BeanUtil.copyToList(this.list(Wrappers.<ExamGroup>lambdaQuery().eq(ExamGroup::getExamId, examId)), ExamGroupDetailResponse.class);
		IdListRequest request = new IdListRequest();
		request.setIdList(courseGroupList.stream().map(ExamGroup::getGroupId).collect(Collectors.toList()));
		List<GroupsDetailResponseDTO> groupList = remoteGroupsService.detailList(request).getData();
		if (CollectionUtils.isNotEmpty(groupList)){
			Map<Long, String> groupMap = groupList.stream().collect(Collectors.toMap(GroupsDetailResponseDTO::getId, GroupsDetailResponseDTO::getGroupName));
			courseGroupList.forEach(i -> i.setGroupName(groupMap.get(i.getGroupId())));
		}
		return courseGroupList;
	}

	@Override
	public ExamGroupDetailResponse detail(Serializable id) {
		return baseMapper.selectDetailById(id);
	}

	@Override
	public boolean update(ExamGroupUpdateRequest params) {
		if (params.getId() == null) {
			throw new IllegalParamsException("ID不能为空！");
		}
		ExamGroup examGroup = new ExamGroup();
		BeanUtils.copyProperties(params, examGroup);
		return this.updateById(examGroup);
	}

	@Override
	public boolean deleteByIds(List<Long> idList) {
		return this.removeByIds(idList);
	}

}

