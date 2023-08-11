package cn.qbs.wa.teach.course.standard.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.course.common.entity.Course;
import cn.qbs.wa.teach.course.common.entity.CourseStudent;
import cn.qbs.wa.teach.course.common.enums.ShelfStatusEnum;
import cn.qbs.wa.teach.course.standard.entity.TCourse;
import cn.qbs.wa.teach.course.standard.entity.TCourseStudent;
import cn.qbs.wa.teach.course.standard.mapper.*;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseLecturerDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.app.PageCourseDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.lite.*;
import cn.qbs.wa.teach.course.standard.pojo.tcoursestudent.TCourseStudentPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.tcoursestudent.TCourseStudentResponse;
import cn.qbs.wa.teach.course.standard.service.LiteCourseService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.lecturer.LecturerDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.lecturer.LecturerSearchDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.organization.OrganizationListInnerResultDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.organization.OrganizationListResultDTO;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniMemberDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LiteCourseServiceImpl implements LiteCourseService {

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private TCourseMapper tCourseMapper;

    @Resource
    private LiteCourseMapper liteCourseMapper;

    @Resource
    private CourseStudentMapper courseStudentMapper;

    @Resource
    private TCourseStudentMapper tCourseStudentMapper;

    @Resource
    private RemoteServiceImpl remoteService;

    @Override
    public IPage<MyCoursePageResponse> pageMyCourse(MyCoursePageRequest params) {
        IPage<MyCoursePageResponse> page = liteCourseMapper.pageMyCourse(params.createMpPage(), params);
        if (!page.getRecords().isEmpty()) {
            List<Long> orgIds = page.getRecords().stream().map(MyCoursePageResponse::getOrgId).collect(Collectors.toList());
            // 远程获取机构信息
            List<OrganizationListInnerResultDTO> listOrg = remoteService.listOrg(orgIds);
            if (CollUtil.isNotEmpty(listOrg)) {
                Map<Long, String> map = listOrg.stream().collect(Collectors.toMap(OrganizationListInnerResultDTO::getId, OrganizationListInnerResultDTO::getName));
                for (MyCoursePageResponse record : page.getRecords()) {
                    record.setOrgName(map.get(record.getOrgId()));
                }
            }
        }
        return page;
    }

    @Override
    public IPage<PageCourseVO> pageCourse(PageCourseDTO params) {
        IPage<PageCourseVO> page = liteCourseMapper.pageCourse(params.createMpPage(), params);
        if (!page.getRecords().isEmpty()) {
            List<Long> orgIds = page.getRecords().stream().map(PageCourseVO::getOrgId).collect(Collectors.toList());
            // 远程获取机构信息
            List<OrganizationListInnerResultDTO> listOrg = remoteService.listOrg(orgIds);
            if (CollUtil.isNotEmpty(listOrg)) {
                Map<Long, String> map = listOrg.stream().collect(Collectors.toMap(OrganizationListInnerResultDTO::getId, OrganizationListInnerResultDTO::getName));
                for (PageCourseVO record : page.getRecords()) {
                    record.setOrgName(map.get(record.getOrgId()));
                }
            }
        }
        return page;
    }

    @Override
    public CourseInfoDTO info(Long courseId) {
        CourseInfoDTO courseInfoDTO = this.liteCourseMapper.selectDetailById(courseId);
        // 下架课程直接返回
        if (ShelfStatusEnum.DOWN.getCode().equals(courseInfoDTO.getShelfStatus())) {
            return courseInfoDTO;
        }
        // 获取机构信息
        courseInfoDTO.setOrgName(getOrgInfo(courseInfoDTO.getOrgId()));
        // 获取讲师信息
        setLecturers(courseInfoDTO);
        return courseInfoDTO;
    }

    @Override
    public CourseInfoDTO courseInfo(Long courseId) {
        CourseInfoDTO courseInfoDTO = this.liteCourseMapper.selectDetailById(courseId);
        // 获取机构信息
        courseInfoDTO.setOrgName(getOrgInfo(courseInfoDTO.getOrgId()));
        // 获取讲师信息
        setLecturers(courseInfoDTO);
        return courseInfoDTO;
    }

    @Override
    public Boolean checkCourseSignUp(Long courseId, Long memberId) {
        Long count = tCourseStudentMapper.selectCount(Wrappers.<TCourseStudent>lambdaQuery().eq(TCourseStudent::getCourseId, courseId).eq(TCourseStudent::getMemberId, memberId));
        if (count == null || count == 0) {
            count = courseStudentMapper.selectCount(Wrappers.<CourseStudent>lambdaQuery().eq(CourseStudent::getCourseId, courseId).eq(CourseStudent::getUserId, memberId));
            if (count == null || count == 0) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean signUp(SignUpDTO request) {
        // 先查询课程是否存在
        Long courseId = request.getCourseId();
        Course course = courseMapper.selectById(courseId);
        if (course == null || Constants.DB_TRUE.equals(course.getDeleted()) || ShelfStatusEnum.DOWN.getCode().equals(course.getShelfStatus())) {
            throw new ServiceException("抱歉，该课程已下架");
        }

        // 预报名课程若不存在,则创建
        Long count = tCourseMapper.selectCount(Wrappers.<TCourse>lambdaQuery().eq(TCourse::getCourseId, courseId));
        if (count == null || count == 0L) {
            TCourse tCourse = new TCourse();
            tCourse.setCourseId(courseId);
            tCourseMapper.insert(tCourse);
        }

        // 注册学员信息
        Long memberId = registerMember(request.getRegister());

        // 课程预报名
        count = tCourseStudentMapper.selectCount(Wrappers.<TCourseStudent>lambdaQuery().eq(TCourseStudent::getCourseId, courseId).eq(TCourseStudent::getMemberId, memberId));
        if (count == null || count == 0) {
            TCourseStudent tCourseStudent = new TCourseStudent();
            tCourseStudent.setMemberId(memberId);
            tCourseStudent.setCourseId(courseId);
            tCourseStudent.setRemark(request.getRegister().getRemark());
            tCourseStudentMapper.insert(tCourseStudent);
        }
        return Boolean.TRUE;
    }

    private Long registerMember(MemberFormDTO form) {
        if (StrUtil.isBlank(form.getOpenId())) {
            throw new ServiceException("缺失小程序 openId");
        }
        if (StrUtil.isBlank(form.getPhone())) {
            throw new ServiceException("请填写手机号码");
        }
        if (StrUtil.isBlank(form.getRealName())) {
            throw new ServiceException("请填写姓名");
        }
        // 注册平台学员用户信息
        return remoteService.registerMember(form);
    }

    private String getOrgInfo(Long orgId) {
        OrganizationListResultDTO org = remoteService.org(orgId);
        return org != null ? org.getName() : null;
    }

    private void setLecturers(CourseInfoDTO courseInfoDTO) {
        if (courseInfoDTO != null && CollUtil.isNotEmpty(courseInfoDTO.getLecturers())) {
            LecturerSearchDTO lecturerSearchDTO = new LecturerSearchDTO();
            lecturerSearchDTO.setOrgId(courseInfoDTO.getOrgId());
            lecturerSearchDTO.setIdList(courseInfoDTO.getLecturers().stream().map(CourseLecturerDTO::getLecturerId).collect(Collectors.toList()));
            List<LecturerDTO> lecturers = remoteService.listLecturers(lecturerSearchDTO);
            if (CollUtil.isNotEmpty(lecturers)) {
                Map<Long, LecturerDTO> dtoMap = lecturers.stream().collect(Collectors.toMap(LecturerDTO::getId, o -> o, (a, b) -> a));
                courseInfoDTO.getLecturers().forEach(e -> {
                    Optional.ofNullable(dtoMap.get(e.getLecturerId())).ifPresent(m -> {
                        e.setLecturerHeadImgUrl(m.getHeadImgUrl());
                        e.setLecturerIntro(m.getIntro());
                        e.setLecturerName(m.getLecturerName());
                        e.setLecturerTitle(m.getTitle());
                    });
                });
            }
        }
    }

    @Override
    public IPage<PageCourseVO> orgPageCourse(PageCourseDTO params) {
        IPage<PageCourseVO> page = liteCourseMapper.orgPageCourse(params.createMpPage(), params);
        List<PageCourseVO> records = page.getRecords();
        if (!records.isEmpty()) {
            // 查询课程讲师
            List<Long> ids = records.stream().map(PageCourseVO::getId).collect(Collectors.toList());
            List<CourseInfoDTO> courseLecturerList = liteCourseMapper.listCourseLecturer(ids);
            if (CollUtil.isNotEmpty(courseLecturerList)) {
                // 课程讲师映射关系
                Map<Long, List<CourseLecturerDTO>> clMap = courseLecturerList.stream().collect(Collectors.toMap(CourseInfoDTO::getCourseId, CourseInfoDTO::getLecturers));
                Long orgId = records.get(0).getOrgId();
                // 远程获取讲师信息
                LecturerSearchDTO lecturerSearchDTO = new LecturerSearchDTO();
                lecturerSearchDTO.setOrgId(orgId);
                List<Long> lecturerIds = clMap.values().stream().flatMap(e -> e.stream().map(CourseLecturerDTO::getLecturerId)).collect(Collectors.toList());
                lecturerSearchDTO.setIdList(lecturerIds);
                List<LecturerDTO> rmLecturers = remoteService.listLecturers(lecturerSearchDTO);
                if (CollUtil.isNotEmpty(rmLecturers)) {
                    Map<Long, LecturerDTO> dtoMap = rmLecturers.stream().collect(Collectors.toMap(LecturerDTO::getId, o -> o, (a, b) -> a));
                    for (PageCourseVO record : records) {
                        if (clMap.containsKey(record.getId())) {
                            List<CourseLecturerDTO> lecturers = clMap.get(record.getId());
                            for (CourseLecturerDTO lecturer : lecturers) {
                                Optional.ofNullable(dtoMap.get(lecturer.getLecturerId())).ifPresent(m -> {
                                    lecturer.setLecturerName(m.getLecturerName());
                                });
                            }
                            record.setLecturers(lecturers);
                        }
                    }
                }
            }
        }
        return page;
    }

    @Override
    public IPage<TCourseStudentResponse> orgPagePreStudent(TCourseStudentPageRequest params) {
        Page<TCourseStudentResponse> page = params.createMpPage();
        tCourseStudentMapper.litePagePreStudent(page, params);
        // 远程查询学员用户信息
        List<TCourseStudentResponse> records = page.getRecords();
        if (!records.isEmpty()) {
            List<Long> memberIds = records.stream().map(TCourseStudentResponse::getMemberId).collect(Collectors.toList());
            Map<Long, UniMemberDTO> memberMap = remoteService.remoteMemberMap(memberIds);
            for (TCourseStudentResponse record : records) {
                Optional.ofNullable(memberMap.get(record.getMemberId())).ifPresent(m -> {
                    record.setRealName(m.getRealName());
                    record.setPhone(m.getPhone());
                });
            }

        }
        return page;
    }
}
