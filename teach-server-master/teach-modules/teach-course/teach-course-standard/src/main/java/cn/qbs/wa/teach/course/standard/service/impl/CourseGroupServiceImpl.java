package cn.qbs.wa.teach.course.standard.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.course.common.entity.CourseDept;
import cn.qbs.wa.teach.course.standard.mapper.CourseGroupMapper;
import cn.qbs.wa.teach.course.common.entity.CourseGroup;
import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageRequest;
import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageResponse;
import cn.qbs.wa.teach.course.standard.pojo.coursedept.CourseDeptAddRequest;
import cn.qbs.wa.teach.course.standard.service.CourseGroupService;
import cn.qbs.wa.teach.course.standard.pojo.coursegroup.*;
import cn.qbs.wa.teach.course.standard.service.CourseLecturerService;
import cn.qbs.wa.teach.course.standard.service.CourseStudentService;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 课程标签表(CourseGroup)表服务实现类
 *
 * @author makejava
 * @since 2022-08-11 15:23:58
 */
@Slf4j
@Service("courseGroupService")
public class CourseGroupServiceImpl extends ServiceImpl<CourseGroupMapper, CourseGroup> implements CourseGroupService {

	@Resource
	private RemoteStudentService remoteStudentService;

	@Resource
	private CourseStudentService courseStudentService;

	@Resource
	private RemoteGroupsService remoteGroupsService;

	@Resource
	private CourseLecturerService courseLecturerService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean add(CourseGroupBatchAddRequest params) {
		List<CourseGroup> courseGroupList = BeanUtil.copyToList(params.getCourseGroupList(), CourseGroup.class);
		this.saveBatch(courseGroupList);
		return addCourseStudentByGroup(BeanUtil.copyToList(courseGroupList, CourseGroupAddRequest.class));
	}

	@Override
	public boolean addCourseStudentByGroup(List<CourseGroupAddRequest> courseGroupAddList){
		List<CourseGroup> courseGroupList = BeanUtil.copyToList(courseGroupAddList, CourseGroup.class);
		List<Long> courseIdList = courseGroupList.stream().map(CourseGroup::getCourseId).distinct().collect(Collectors.toList());
		List<Long> groupIdList = courseGroupList.stream().map(CourseGroup::getGroupId).distinct().collect(Collectors.toList());
		//远程查询部门下的学员
		StudentSearchDTO searchDTO = new StudentSearchDTO();
		searchDTO.setGroupIdList(groupIdList);
		R<List<StudentDTO>> studentList = remoteStudentService.list(searchDTO);
		if (!studentList.isOk()){
			throw new ServiceException("远程服务失败！");
		}
		return courseStudentService.addCourseStudent(courseIdList, studentList.getData());
	}

	@Override
	public Long groupCount(List<Long> groupIdList) {
		return this.baseMapper.groupCount(groupIdList);
	}

	@Override
	public List<CourseGroupDetailResponse> listAssignGroup(Long courseId) {
		List<CourseGroupDetailResponse> courseGroupList = BeanUtil.copyToList(this.list(Wrappers.<CourseGroup>lambdaQuery().eq(CourseGroup::getCourseId, courseId)), CourseGroupDetailResponse.class);
		IdListRequest groupIdList = new IdListRequest();
		groupIdList.setIdList(courseGroupList.stream().map(CourseGroup::getGroupId).collect(Collectors.toList()));
		List<GroupsDetailResponseDTO> groupList = remoteGroupsService.detailList(groupIdList).getData();
		if (CollectionUtils.isNotEmpty(groupList)){
			Map<Long, String> groupMap = groupList.stream().collect(Collectors.toMap(GroupsDetailResponseDTO::getId, GroupsDetailResponseDTO::getGroupName));
			courseGroupList.forEach(i -> i.setGroupName(groupMap.get(i.getGroupId())));
		}
		return courseGroupList;
	}

	@Override
	public IPage<CoursePageResponse> page(CoursePageRequest params) {
		IPage<CoursePageResponse> page = null;
		if (StrUtil.isNotBlank(params.getLecturerName())) {
			// 讲师名称搜索
			page = this.baseMapper.pageByGroupAndLecturer(params.createMpPage(), params);
		} else {
			page = this.baseMapper.pageByGroup(params.createMpPage(), params);

		}
		if (CollectionUtils.isNotEmpty(page.getRecords())){
			for (CoursePageResponse record : page.getRecords()) {
				// 获取课程讲师
				record.setLecturers(courseLecturerService.listByCourseId(record.getId()));
				if (record.getGroupIds() == null || params.getGroupId() == null){
					continue;
				}
				//获取包含的标签id并以","分割为数组
				String[] groupIds = record.getGroupIds().split(",");
				//将部门id数组转为List，在判断是否包含前端传来的标签id
				if (Arrays.asList(groupIds).contains(params.getGroupId().toString())){
					record.setAdded(0);
				}
			}
		}
		return page;
	}

	@Override
	public CourseGroupDetailResponse detail(Serializable id) {
		return baseMapper.selectDetailById(id);
	}

	@Override
	public boolean update(CourseGroupUpdateRequest params) {
		if (params.getId() == null) {
			throw new IllegalParamsException("ID不能为空！");
		}
		CourseGroup courseGroup = new CourseGroup();
		BeanUtils.copyProperties(params, courseGroup);
		return this.updateById(courseGroup);
	}

	@Override
	public boolean deleteByIds(List<Long> idList, Long groupId) {
		if (groupId == null){
			return this.removeByIds(idList);
		}else {
			return this.remove(Wrappers.<CourseGroup>lambdaQuery().in(CourseGroup::getCourseId, idList).eq(CourseGroup::getGroupId, groupId));
		}
	}

}

