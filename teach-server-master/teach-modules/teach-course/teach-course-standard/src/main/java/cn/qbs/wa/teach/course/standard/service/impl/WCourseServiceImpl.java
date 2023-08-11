package cn.qbs.wa.teach.course.standard.service.impl;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
//import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
//import cn.qbs.wa.teach.course.common.entity.Course;
//import cn.qbs.wa.teach.course.common.entity.CourseComponent;
//import cn.qbs.wa.teach.course.common.entity.CourseLiveLink;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.course.common.entity.Course;
import cn.qbs.wa.teach.course.common.entity.WCourse;
//import cn.qbs.wa.teach.course.common.enums.ComponentTypeEnum;
//import cn.qbs.wa.teach.course.common.enums.CourseTypeEnum;
//import cn.qbs.wa.teach.course.standard.mapper.CourseComponentMapper;
//import cn.qbs.wa.teach.course.standard.mapper.CourseLiveLinkMapper;
//import cn.qbs.wa.teach.course.standard.mapper.CourseMapper;
import cn.qbs.wa.teach.course.common.enums.ShelfStatusEnum;
import cn.qbs.wa.teach.course.standard.mapper.CourseMapper;
import cn.qbs.wa.teach.course.standard.mapper.WCourseMapper;
//import cn.qbs.wa.teach.course.standard.pojo.wcourse.*;
import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageResponse;
import cn.qbs.wa.teach.course.standard.pojo.wcourse.WCoursePageRequest;
import cn.qbs.wa.teach.course.standard.pojo.wcourse.WCoursePageResponse;
import cn.qbs.wa.teach.course.standard.pojo.wcourse.WCourseUpdateRequest;
import cn.qbs.wa.teach.course.standard.service.CourseCategoryService;
import cn.qbs.wa.teach.course.standard.service.CourseLecturerService;
import cn.qbs.wa.teach.course.standard.service.WCourseService;
//import cn.qbs.wa.teach.course.live.api.RemoteWLiveService;
//import cn.qbs.wa.teach.course.live.api.enums.LiveBusinessTypeEnum;
//import cn.qbs.wa.teach.course.live.api.pojo.DTO.wlive.WLiveAddDTO;
//import cn.qbs.wa.teach.course.live.api.pojo.DTO.wlive.WLiveAddDetail;
//import cn.qbs.wa.teach.course.live.api.pojo.DTO.wlive.WLiveDeleteByBusinessDTO;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import cn.qbs.wa.teach.organization.api.RemoteOrgService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.organization.OrganizationInnerDetailResponseDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * 插件-课程表(WCourse)表服务实现类
// *
// * @author makejava
// * @since 2022-03-01 14:25:16
// */
//@Slf4j
@Service("wCourseService")
public class WCourseServiceImpl extends ServiceImpl<WCourseMapper, WCourse> implements WCourseService {
//
//
//    @Autowired
//    RemoteWLiveService remoteWLiveService;
//
//    @Autowired
//    CourseComponentMapper courseComponentMapper;
//
    @Autowired
    CourseMapper courseMapper;

    @Autowired
    RemoteOrgService remoteOrgService;

    @Resource
    private CourseLecturerService courseLecturerService;

//
//    @Autowired
//    CourseLiveLinkMapper courseLiveLinkMapper;
//
//
//    @Override
//    public boolean add(WCourseAddRequest params) {
//        List<Long> idList = params.getIdList();
//        List<WCourse> existCourseList = listByIds(idList);
//        if(CollUtil.isNotEmpty(existCourseList)){
//            idList.removeAll(existCourseList.stream().map(WCourse::getId).collect(Collectors.toList()));
//        }
//        if (CollUtil.isNotEmpty(idList)) {
//            List<WCourse> wCourses = new ArrayList<>();
//            WLiveAddDTO wLiveAddDTO = new WLiveAddDTO();
//            List<WLiveAddDetail> wLiveAddDetails = new ArrayList<>();
//            for (Long courseId : idList) {
//                Course course = courseMapper.selectByIdWithoutOrg(courseId);
//                SecurityContextHolder.setSelectOrgId(course.getOrgId().toString());
//                if (course != null && !CourseTypeEnum.RECORD.getCode().equals(course.getCourseType())) {
//                    addCourseLiveLink(wLiveAddDetails, course);
//                    addCourseComponentLiveLink(wLiveAddDetails, course);
//                }
//                WCourse wCourse = new WCourse();
//                wCourse.setId(course.getId());
//                wCourse.setOrgId(course.getOrgId());
//                wCourses.add(wCourse);
//            }
//            saveBatch(wCourses);
//
//            if (CollUtil.isNotEmpty(wLiveAddDetails)) {
//                wLiveAddDTO.setLiveAddDetailList(wLiveAddDetails);
//                remoteWLiveService.add(wLiveAddDTO);
//            }
//
//        }
//        return true;
//    }
//
//
//
//    private void addCourseComponentLiveLink(List<WLiveAddDetail> wLiveAddDetails, Course course) {
//        List<CourseComponent> courseComponentList = courseComponentMapper.selectList(new LambdaQueryWrapper<CourseComponent>().eq(CourseComponent::getCourseId, course.getId()).eq(CourseComponent::getComponentTypeCode, ComponentTypeEnum.ZB.getCode()));
//        if (CollUtil.isNotEmpty(courseComponentList)) {
//            for (CourseComponent courseComponent : courseComponentList) {
//                WLiveAddDetail wLiveAddDetail = new WLiveAddDetail();
//                wLiveAddDetail.setOrgId(course.getOrgId());
//                wLiveAddDetail.setBusinessId(courseComponent.getId());
//                wLiveAddDetail.setBusinessType(LiveBusinessTypeEnum.COURSE_CHILD.getId());
//                wLiveAddDetail.setBusinessName(courseComponent.getComponentName());
//                wLiveAddDetails.add(wLiveAddDetail);
//            }
//        }
//    }
//
//
//    private void addCourseLiveLink(List<WLiveAddDetail> wLiveAddDetails, Course course) {
//        List<CourseLiveLink> courseLiveLinkList = courseLiveLinkMapper.selectList(new LambdaQueryWrapper<CourseLiveLink>().eq(CourseLiveLink::getCourseId, course.getId()));
//        if (CollUtil.isNotEmpty(courseLiveLinkList)) {
//            for (CourseLiveLink courseLiveLink : courseLiveLinkList) {
//                WLiveAddDetail wLiveAddDetail = new WLiveAddDetail();
//                wLiveAddDetail.setOrgId(course.getOrgId());
//                wLiveAddDetail.setBusinessId(courseLiveLink.getCourseId());
//                wLiveAddDetail.setBusinessType(LiveBusinessTypeEnum.COURSE.getId());
//                wLiveAddDetail.setBusinessName(course.getCourseName());
//                wLiveAddDetails.add(wLiveAddDetail);
//            }
//        }
//    }
//
    @Override
    public IPage<WCoursePageResponse> pageV2(WCoursePageRequest params) {
        Page<WCoursePageResponse> page = params.createMpPage();
        if (StrUtil.isNotBlank(params.getLecturerName())) {
            // 讲师名称搜索
            this.baseMapper.pageByLecturer(page, params);
        } else {
            this.baseMapper.page(page, params);
        }
        for (WCoursePageResponse wCoursePageResponse:page.getRecords()) {
            IdRequest idRequest=new IdRequest();
            idRequest.setId(wCoursePageResponse.getOrgId());
            OrganizationInnerDetailResponseDTO organizationInnerDetailResponseDTO=remoteOrgService.detail(idRequest).getRemoteData();
            wCoursePageResponse.setOrgName(organizationInnerDetailResponseDTO.getName());
            wCoursePageResponse.setLecturers(courseLecturerService.listByCourseId(wCoursePageResponse.getCourseId()));
        }
        return page;
    }

    @Override
    public IPage<WCoursePageResponse> page(WCoursePageRequest params) {
        IPage<WCoursePageResponse> wCoursePageResponseIPage=baseMapper.page(params.createMpPage(), params);
        for (WCoursePageResponse wCoursePageResponse:wCoursePageResponseIPage.getRecords()) {
            IdRequest idRequest=new IdRequest();
            idRequest.setId(wCoursePageResponse.getOrgId());
            OrganizationInnerDetailResponseDTO organizationInnerDetailResponseDTO=remoteOrgService.detail(idRequest).getRemoteData();
            wCoursePageResponse.setOrgName(organizationInnerDetailResponseDTO.getName());
        }
        return wCoursePageResponseIPage;
    }
//
//    @Override
//    public IPage<WCoursePageResponse> search(WCoursePageRequest params) {
//        return baseMapper.search(params.createMpPage(), params);
//    }
//
//    @Override
//    public WCourseDetailResponse detail(Serializable id) {
//        return baseMapper.selectDetailById(id);
//    }
//
    @Override
    public boolean update(WCourseUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
//        Course course = courseMapper.selectByIdWithoutOrg(params.getId());
//        SecurityContextHolder.setSelectOrgId(course.getOrgId().toString());
        WCourse wCourse = new WCourse();
        BeanUtils.copyProperties(params, wCourse);
//        wCourse.setOrgId(course.getOrgId());
        return this.updateById(wCourse);
    }
//
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByIds(List<Long> idList) {
        if (idList == null || idList.isEmpty()) {
            throw new IllegalParamsException("ID不能为空！");
        }
        boolean flag = this.removeByIds(idList);
        Course course=new Course();
        course.setId(idList.get(Constants.DB_FAIL));
        course.setShelfStatus(ShelfStatusEnum.DOWN.getCode());
        courseMapper.updateShelfStatus(course);
//        List<WLiveAddDetail> wLiveAddDetails = new ArrayList<>();
//        for (Long courseId : idList) {
//            Course course = courseMapper.selectByIdWithoutOrg(courseId);
//            if (course != null && !CourseTypeEnum.RECORD.getCode().equals(course.getCourseType())) {
//                SecurityContextHolder.setSelectOrgId(course.getOrgId().toString());
//                addCourseLiveLink(wLiveAddDetails, course);
//                addCourseComponentLiveLink(wLiveAddDetails, course);
//            }
//        }
//        if (CollUtil.isNotEmpty(wLiveAddDetails)) {
//            Map<Integer, List<WLiveAddDetail>> collect = wLiveAddDetails.stream().collect(Collectors.groupingBy(WLiveAddDetail::getBusinessType));
//            for (Integer businessTypeId : collect.keySet()) {
//                WLiveDeleteByBusinessDTO wLiveDeleteByBusinessDTO = new WLiveDeleteByBusinessDTO();
//                wLiveDeleteByBusinessDTO.setBusinessType(businessTypeId);
//                wLiveDeleteByBusinessDTO.setIdList(collect.get(businessTypeId).stream().map(WLiveAddDetail::getBusinessId).collect(Collectors.toList()));
//                remoteWLiveService.deleteByBusiness(wLiveDeleteByBusinessDTO);
//            }
//        }
        return flag;
    }
//
//    @Override
//    public IPage<WCoursePageByChildResponse> pageByChild(WCoursePageRequest params) {
//        return baseMapper.pageByChild(params.createMpPage(), params);
//    }
//
}
//
