package cn.qbs.wa.teach.course.standard.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.course.common.entity.CourseStudent;
import cn.qbs.wa.teach.course.standard.mapper.CourseDeptMapper;
import cn.qbs.wa.teach.course.common.entity.CourseDept;
import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageRequest;
import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageResponse;
import cn.qbs.wa.teach.course.standard.service.CourseDeptService;
import cn.qbs.wa.teach.course.standard.pojo.coursedept.*;
import cn.qbs.wa.teach.course.standard.service.CourseLecturerService;
import cn.qbs.wa.teach.course.standard.service.CourseStudentService;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 课程部门表(CourseDept)表服务实现类
 *
 * @author makejava
 * @since 2022-08-10 19:27:55
 */
@Slf4j
@Service("courseDeptService")
public class CourseDeptServiceImpl extends ServiceImpl<CourseDeptMapper, CourseDept> implements CourseDeptService {

	@Resource
	private RemoteStudentService remoteStudentService;

	@Resource
	private RemoteOrgBackDeptService remoteOrgBackDeptService;

	@Resource
	private CourseStudentService courseStudentService;

	@Resource
	private CourseLecturerService courseLecturerService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean add(CourseDeptBatchAddRequest params) {
		Long orgId = SecurityContextHolder.getOrgId();
		List<CourseDept> courseDeptList = BeanUtil.copyToList(params.getCourseDeptList().stream().filter(i -> i.getDeptName() != null).collect(Collectors.toList()), CourseDept.class);
		courseDeptList.forEach(i -> i.setOrgId(orgId));
		this.saveBatch(courseDeptList);
		return addCourseStudentByDept(BeanUtil.copyToList(params.getCourseDeptList(), CourseDept.class), orgId);
	}

	@Override
	public boolean addCourseStudentByDept(List<CourseDept> courseDeptList, Long orgId){
		List<Long> courseIdList = courseDeptList.stream().map(CourseDept::getCourseId).distinct().collect(Collectors.toList());
		List<Long> deptIdList = courseDeptList.stream().map(CourseDept::getDeptId).distinct().collect(Collectors.toList());
		//远程查询部门下的学员
		StudentSearchDTO searchDTO = new StudentSearchDTO();
		searchDTO.setOrgId(orgId);
		searchDTO.setDeptIdList(deptIdList);
		R<List<StudentDTO>> studentList = remoteStudentService.list(searchDTO);
		if (!studentList.isOk()){
			throw new ServiceException("远程服务失败！");
		}
		return courseStudentService.addCourseStudent(courseIdList, studentList.getData());
	}

	@Override
	public List<CourseDeptDetailResponse> listAssignDept(Long courseId) {
		List<CourseDeptDetailResponse> courseDeptList = BeanUtil.copyToList(this.list(Wrappers.<CourseDept>lambdaQuery().eq(CourseDept::getCourseId, courseId).orderByAsc(CourseDept::getCreateTime)), CourseDeptDetailResponse.class);
		IdListRequest request = new IdListRequest();
		request.setIdList(courseDeptList.stream().map(CourseDept::getDeptId).collect(Collectors.toList()));
		List<DeptDetailResponseDTO> deptList = remoteOrgBackDeptService.detailList(request).getData();
		if (CollectionUtils.isNotEmpty(deptList)){
			Map<Long, DeptDetailResponseDTO> deptMap = deptList.stream().collect(Collectors.toMap(DeptDetailResponseDTO::getId, o -> o));
			courseDeptList.forEach(i -> i.setDeptName(deptMap.get(i.getDeptId()).getDeptName()));
			courseDeptList.forEach(i -> i.setParentId(deptMap.get(i.getDeptId()).getParentId()));
		}
		return courseDeptList;
	}

	@Override
	public Long deptCount(List<Long> deptIdList) {
		return this.baseMapper.deptCount(deptIdList);
	}

	@Override
	public IPage<CoursePageResponse> page(CoursePageRequest params) {
		IPage<CoursePageResponse> page = null;
		if (StrUtil.isNotBlank(params.getLecturerName())) {
			// 讲师名称搜索
			page = this.baseMapper.pageByDeptAndLecturer(params.createMpPage(), params);
		} else {
			page = this.baseMapper.pageByDept(params.createMpPage(), params);

		}
		if (CollectionUtils.isNotEmpty(page.getRecords())){
			for (CoursePageResponse record : page.getRecords()) {
				// 获取课程讲师
				record.setLecturers(courseLecturerService.listByCourseId(record.getId()));
				if (record.getDeptIds() == null || params.getDeptId() == null){
					continue;
				}
				//获取包含的部门id并以","分割为数组
				String[] deptIds = record.getDeptIds().split(",");
				//将部门id数组转为List，在判断是否包含前端传来的部门id
				if (Arrays.asList(deptIds).contains(params.getDeptId().toString())){
					record.setAdded(0);
				}
			}
		}
		return page;
	}

	@Override
	public CourseDeptDetailResponse detail(Serializable id) {
		return baseMapper.selectDetailById(id);
	}

	@Override
	public boolean update(CourseDeptUpdateRequest params) {
		if (params.getId() == null) {
			throw new IllegalParamsException("ID不能为空！");
		}
		CourseDept courseDept = new CourseDept();
		BeanUtils.copyProperties(params, courseDept);
		return this.updateById(courseDept);
	}

	@Override
	public boolean deleteByIds(List<Long> idList, Long deptId) {
		if (deptId == null){
			return this.removeByIds(idList);
		}else {
			return this.remove(Wrappers.<CourseDept>lambdaQuery().in(CourseDept::getCourseId, idList).eq(CourseDept::getDeptId, deptId));
		}
	}

}

