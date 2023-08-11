package cn.qbs.wa.teach.course.standard.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdOrgRequest;
import cn.qbs.wa.teach.course.common.entity.CourseUserDeptVisible;
import cn.qbs.wa.teach.course.common.entity.CourseUserGroupVisible;
import cn.qbs.wa.teach.course.common.entity.CourseUserVisible;
import cn.qbs.wa.teach.course.common.enums.UserVisibleStatusEnum;
import cn.qbs.wa.teach.course.standard.mapper.CourseUserVisibleMapper;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseInfoDTO;
import cn.qbs.wa.teach.course.standard.service.CommonService;
import cn.qbs.wa.teach.course.standard.service.CourseUserDeptVisibleService;
import cn.qbs.wa.teach.course.standard.service.CourseUserGroupVisibleService;
import cn.qbs.wa.teach.organization.api.RemoteStudentService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommonServiceImpl implements CommonService {

    @Resource
    private CourseUserVisibleMapper courseUserVisibleMapper;

    @Resource
    private CourseUserDeptVisibleService courseUserDeptVisibleService;

    @Resource
    private CourseUserGroupVisibleService courseUserGroupVisibleService;

    @Resource
    private RemoteStudentService remoteStudentService;

    @Override
    public void setCourseVisible(CourseInfoDTO info, Long userId) {
        if (UserVisibleStatusEnum.ALL.getCode().equals(info.getUserVisibleStatus())) {
            info.setCourseVisibleStatus(1);
            return;
        }
        Long courseId = info.getCourseId();
        Long aLong = this.courseUserVisibleMapper.selectCount(new LambdaQueryWrapper<CourseUserVisible>().eq(CourseUserVisible::getCourseId, courseId).eq(CourseUserVisible::getUserId, userId));
        if (aLong > 0) {
            info.setCourseVisibleStatus(1);
            return;
        }
        //查询学员的标签和部门
        IdOrgRequest idOrgRequest = new IdOrgRequest();
        idOrgRequest.setId(SecurityContextHolder.getStudentId());
        idOrgRequest.setOrgId(SecurityContextHolder.getOrgId());
        StudentDTO remoteData = remoteStudentService.details(idOrgRequest).getRemoteData();
        if (remoteData != null) {
            if (remoteData.getDeptId() != null) {
                //查询课程绑定的部门id
                List<CourseUserDeptVisible> courseUserDeptVisibleList = this.courseUserDeptVisibleService.list(new LambdaQueryWrapper<CourseUserDeptVisible>().select(CourseUserDeptVisible::getDeptId).eq(CourseUserDeptVisible::getCourseId, courseId));
                if (!courseUserDeptVisibleList.isEmpty()) {
                    List<Long> deptIds = courseUserDeptVisibleList.stream().map(CourseUserDeptVisible::getDeptId).collect(Collectors.toList());
                    List<Long> objects = new ArrayList<>();
                    objects.add(remoteData.getDeptId());
                    if (CollUtil.isNotEmpty(remoteData.getDeptPidList())) {
                        objects.addAll(remoteData.getDeptPidList());
                    }
                    if (!CollUtil.intersection(deptIds, objects).isEmpty()) {
                        info.setCourseVisibleStatus(1);
                        return;
                    }
                }
            }
            if (CollUtil.isNotEmpty(remoteData.getGroupIdList())) {
                //查询课程绑定的标签id
                List<CourseUserGroupVisible> courseUserGroupVisibleList = this.courseUserGroupVisibleService.list(new LambdaQueryWrapper<CourseUserGroupVisible>().select(CourseUserGroupVisible::getGroupId).eq(CourseUserGroupVisible::getCourseId, courseId));
                List<Long> groupIds = courseUserGroupVisibleList.stream().map(CourseUserGroupVisible::getGroupId).collect(Collectors.toList());
                List<Long> collect = groupIds.stream().filter(f -> remoteData.getGroupIdList().contains(f)).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(collect)) {
                    info.setCourseVisibleStatus(1);
                }
            }
        }
    }

}
