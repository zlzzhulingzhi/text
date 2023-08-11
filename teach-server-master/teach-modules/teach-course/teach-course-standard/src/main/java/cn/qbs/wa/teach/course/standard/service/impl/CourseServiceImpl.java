package cn.qbs.wa.teach.course.standard.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.enums.LevelEnum;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.course.common.entity.*;
import cn.qbs.wa.teach.course.common.enums.ComponentTypeEnum;
import cn.qbs.wa.teach.course.common.enums.CourseTypeEnum;
import cn.qbs.wa.teach.course.common.enums.UserVisibleStatusEnum;
import cn.qbs.wa.teach.course.standard.entity.TCourseStudent;
import cn.qbs.wa.teach.course.standard.mapper.CourseCategoryMapper;
import cn.qbs.wa.teach.course.standard.mapper.CourseMapper;
import cn.qbs.wa.teach.course.standard.mapper.CourseUserDeptVisibleMapper;
import cn.qbs.wa.teach.course.standard.mapper.CourseUserGroupVisibleMapper;
import cn.qbs.wa.teach.course.standard.pojo.course.*;
import cn.qbs.wa.teach.course.standard.pojo.coursedept.CourseDeptBatchAddRequest;
import cn.qbs.wa.teach.course.standard.pojo.dto.*;
import cn.qbs.wa.teach.course.standard.service.*;
import cn.qbs.wa.teach.organization.api.RemoteGroupsService;
import cn.qbs.wa.teach.organization.api.RemoteOrgBackDeptService;
import cn.qbs.wa.train.logistics.api.pojo.DTO.ClazzPageRequestDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 【课程】(Course)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@Slf4j
@Service("courseService")
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Resource
    private RemoteService remoteService;
    @Resource
    private CourseInfoService courseInfoService;
    @Resource
    private CourseCategoryService courseCategoryService;
    @Resource
    private CourseLecturerService courseLecturerService;
    @Resource
    private CourseUserVisibleService courseUserVisibleService;
    @Resource
    private CourseStatisticOverviewService courseStatisticOverviewService;
    @Resource
    private CourseChapterService courseChapterService;
    @Resource
    private CourseLessonService courseLessonService;
    @Resource
    private CourseComponentService courseComponentService;
    @Resource
    private CourseComponentAttachmentService courseComponentAttachmentService;
    @Resource
    private CourseStudentService courseStudentService;
    @Resource
    private CourseLiveLinkService courseLiveLinkService;

    @Resource
    private CourseUserDeptVisibleService courseUserDeptVisibleService;

    @Resource
    private CourseUserDeptVisibleMapper courseUserDeptVisibleMapper;

    @Resource
    private CourseUserGroupVisibleService courseUserGroupVisibleService;

    @Resource
    private CourseUserGroupVisibleMapper courseUserGroupVisibleMapper;

    @Resource
    private RemoteOrgBackDeptService remoteOrgBackDeptService;

    @Resource
    private RemoteGroupsService remoteGroupsService;

    @Resource
    private CourseCategoryMapper courseCategoryMapper;

    @Resource
    private TCourseStudentService tCourseStudentService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Course add(CourseAddRequest params) {
        Course course = new Course();
        BeanUtils.copyProperties(params, course);
        boolean save = this.save(course);
        if (save) {
            Long courseId = course.getId();
            // 保存课程信息
            CourseInfo courseInfo = new CourseInfo();
            BeanUtil.copyProperties(params, courseInfo);
            courseInfo.setCourseId(courseId);
            this.courseInfoService.save(courseInfo);
            // 保存讲师
            params.getLecturers().forEach(courseLecturer -> {
                courseLecturer.setCourseId(courseId);
                courseLecturer.setId(null);
                courseLecturer.setOrgId(null);
            });
            this.courseLecturerService.saveBatch(params.getLecturers());
            // 保存可见用户
            if (CollUtil.isNotEmpty(params.getVisibleUsers())) {
                params.getVisibleUsers().forEach(user -> {
                    user.setCourseId(courseId);
                    user.setId(null);
                    user.setOrgId(null);
                });
                this.courseUserVisibleService.saveBatch(params.getVisibleUsers());
            }
            // 保存可见部门
            if (CollUtil.isNotEmpty(params.getCourseUserDeptVisibles())) {
                params.getCourseUserDeptVisibles().forEach(dept -> {
                    dept.setCourseId(courseId);
                    dept.setId(null);
                    dept.setOrgId(null);
                });
                this.courseUserDeptVisibleService.saveBatch(params.getCourseUserDeptVisibles());
            }
            // 保存可见标签
            if (CollUtil.isNotEmpty(params.getCourseUserGroupVisibles())) {
                params.getCourseUserGroupVisibles().forEach(group -> {
                    group.setCourseId(courseId);
                    group.setId(null);
                    group.setOrgId(null);
                });
                this.courseUserGroupVisibleService.saveBatch(params.getCourseUserGroupVisibles());
            }
            // 创建一条课程统计信息
            CourseStatisticOverview statisticOverview = new CourseStatisticOverview();
            statisticOverview.setCourseId(courseId);
            this.courseStatisticOverviewService.save(statisticOverview);
            // 保存课程分类
            for (Long categoryId : params.getCategoryIds()) {
                this.saveCourseCategory(courseId, categoryId);
            }
        }
        return course;
    }

    @Override
    public IPage<CoursePageResponse> page(CoursePageRequest params) {
        Page<CoursePageResponse> page = params.createMpPage();
        page.addOrder(new OrderItem("id", false));
        if (StrUtil.isNotBlank(params.getLecturerName())) {
            // 讲师名称搜索
            this.baseMapper.pageByLecturer(page, params);
        } else {
            this.baseMapper.page(page, params);
        }
        // 获取课程讲师
        for (CoursePageResponse record : page.getRecords()) {
            record.setLecturers(courseLecturerService.listByCourseId(record.getId()));
        }
        return page;
    }

    @Override
    public IPage<CoursePageResponse> pageByDept(CoursePageRequest params) {
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
    public IPage<CoursePageResponse> pageByGroup(CoursePageRequest params) {
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
    public boolean deptBatchAdd(CourseDeptBatchAddRequest params) {
        List<CourseUserDeptVisible> courseUserDeptVisibleList = params.getCourseDeptList().stream().map(i -> {
            CourseUserDeptVisible courseUserDeptVisible = new CourseUserDeptVisible();
            BeanUtils.copyProperties(i, courseUserDeptVisible);
            return courseUserDeptVisible;
        }).collect(Collectors.toList());
        return courseUserDeptVisibleService.saveBatch(courseUserDeptVisibleList);
    }

    @Override
    public boolean deptBatchRemove(CourseRemoveDeptRequest params) {
        if (params.getOrgId() == null){
            params.setOrgId(SecurityContextHolder.getOrgId());
        }
        courseUserDeptVisibleMapper.deptBatchRemove(params);
        return true;
    }

    @Override
    public boolean groupBatchAdd(CourseAddGroupRequest params) {
        List<CourseUserGroupVisible> courseUserGroupVisibles = params.getCourseId().stream().map(i -> {
            CourseUserGroupVisible courseUserGroupVisible = new CourseUserGroupVisible();
            BeanUtils.copyProperties(params, courseUserGroupVisible);
            courseUserGroupVisible.setCourseId(i);
            return courseUserGroupVisible;
        }).collect(Collectors.toList());
        return courseUserGroupVisibleService.saveBatch(courseUserGroupVisibles);
    }

    @Override
    public boolean groupBatchRemove(CourseRemoveGroupRequest params) {
        if (params.getOrgId() == null){
            params.setOrgId(SecurityContextHolder.getOrgId());
        }
        courseUserGroupVisibleMapper.groupBatchRemove(params);
        return true;
    }

    @Override
    public IPage<CoursePageByLecturerResponse> myCoursePage(CoursePageByLecturerRequest params) {
        return this.baseMapper.myCoursePage(params.createMpPage(), params);
    }


    @Override
    public CourseDetailResponse getCourseName(Long id) {
        CourseDetailResponse courseDetailResponse = this.baseMapper.getCourseName(id);
        return courseDetailResponse;
    }

    @Override
    public List<CourseDetailResponse> getCourse(CourseListRequest params) {
        List<CourseDetailResponse> courseDetailResponse = this.baseMapper.getCourse(params);
        return courseDetailResponse;
    }

    @Override
    public CourseDetailResponse detail(Long id) {
        CourseDetailResponse courseDetailResponse = this.baseMapper.selectDetailById(id);
        //if (courseDetailResponse.getCourseManage() != null && courseDetailResponse.getCourseManage() == CourseConstants.COURSE_MANAGE_CLOSE) {
        //    CourseLiveLink liveLink = this.courseLiveLinkService.getLiveLinkByCourseId(id);
        //    if (liveLink != null) {
        //        courseDetailResponse.setLiveId(liveLink.getLiveId());
        //        LiveResultDTO liveResultDTO = remoteService.remoteLive(liveLink.getLiveId());
        //        if (liveResultDTO != null) {
        //            BeanUtil.copyProperties(liveResultDTO, courseDetailResponse, "id", "sort","coverUrl");
        //        }
        //    }
        //}
        // 查询分类列表
        List<CourseCategoryDTO> courseCategories = this.courseCategoryService.listByCourseId(id);
        courseDetailResponse.setCategoryIds(CollStreamUtil.toSet(courseCategories, CourseCategoryDTO::getCategoryId));
        courseDetailResponse.setCategoryNames(CollStreamUtil.toSet(courseCategories, CourseCategoryDTO::getCategoryName));
        //if (UserVisibleStatusEnum.SOME.getCode().equals(courseDetailResponse.getUserVisibleStatus())) {
        //    // 查询可见用户
        //    List<CourseUserVisible> userVisibleList = this.courseUserVisibleService.listByCourseId(id);
        //    if (!userVisibleList.isEmpty()) {
        //        // 远程获取用户信息
        //        List<Long> userIds = userVisibleList.stream().map(CourseUserVisible::getUserId).collect(Collectors.toList());
        //        Map<Long, StudentDTO> finalUserMap = this.remoteService.remoteStudentMap(userIds);
        //        List<CourseUserVisibleDTO> dtoList = userVisibleList.stream().map(e -> {
        //            CourseUserVisibleDTO userVisibleDTO = new CourseUserVisibleDTO();
        //            BeanUtils.copyProperties(e, userVisibleDTO);
        //            Optional.ofNullable(finalUserMap.get(e.getUserId())).ifPresent(user -> {
        //                userVisibleDTO.setRealName(user.getRealName());
        //                userVisibleDTO.setPhone(user.getPhone());
        //            });
        //            return userVisibleDTO;
        //        }).collect(Collectors.toList());
        //        courseDetailResponse.setUserVisibleList(dtoList);
        //    }
        //    // 查询可见部门
        //    List<CourseUserDeptVisible> courseUserDeptVisibleList = this.courseUserDeptVisibleService.listByCourseId(id);
        //    if(CollUtil.isNotEmpty(courseUserDeptVisibleList)){
        //        List<Long> deptIds = courseUserDeptVisibleList.stream().map(CourseUserDeptVisible::getDeptId).collect(Collectors.toList());
        //        IdListRequest request = new IdListRequest();
        //        request.setIdList(deptIds);
        //        R<List<DeptDetailResponseDTO>> remoteData = remoteOrgBackDeptService.detailList(request);
        //        if (CollUtil.isNotEmpty(remoteData.getData())) {
        //            remoteData.getData().forEach(e -> e.setDeptId(e.getId()));
        //        }
        //        courseDetailResponse.setDeptDetailResponseDTOList(remoteData.getData());
        //    }
        //    // 查询可见标签
        //    List<CourseUserGroupVisible> courseUserGroupVisibleList = this.courseUserGroupVisibleService.listByCourseId(id);
        //    if(CollUtil.isNotEmpty(courseUserGroupVisibleList)){
        //        List<Long> groupIds = courseUserGroupVisibleList.stream().map(CourseUserGroupVisible::getGroupId).collect(Collectors.toList());
        //        IdListRequest request = new IdListRequest();
        //        request.setIdList(groupIds);
        //        R<List<GroupsDetailResponseDTO>> remoteData = remoteGroupsService.detailList(request);
        //        if (CollUtil.isNotEmpty(remoteData.getData())) {
        //            remoteData.getData().forEach(e -> e.setGroupId(e.getId()));
        //        }
        //        courseDetailResponse.setGroupsDetailResponseDTOList(remoteData.getData());
        //    }
        //}
        //筛选可见部门，前端只需要展示被选中的父级部门，如果下面有子级则不展示子级
//        if (CollectionUtils.isNotEmpty(courseDetailResponse.getDeptDetailResponseDTOList())){
//            List<Long> visibleDeptIdList = courseDetailResponse.getDeptDetailResponseDTOList().stream().map(DeptDetailResponseDTO::getId).collect(Collectors.toList());
//            courseDetailResponse.setDeptDetailResponseDTOList(
//                    courseDetailResponse.getDeptDetailResponseDTOList().stream()
//                            .filter(i -> !visibleDeptIdList.contains(i.getParentId()))
//                            .collect(Collectors.toList())
//            );
//        }
        return courseDetailResponse;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(CourseUpdateRequest params) {
        Long courseId = params.getId();
        Course course = this.getById(courseId);
        if (course == null) {
            throw new ServiceException("该课程不存在，请刷新重试！");
        }

        List<CourseCategoryDTO> courseCategories = this.courseCategoryService.listByCourseId(courseId);
        Set<Long> oldCategoryIds = courseCategories.stream().map(CourseCategoryDTO::getCategoryId).collect(Collectors.toSet());
        Set<Long> newCategoryIds = params.getCategoryIds();

        // 差集，需要删除的分类
        Collection<Long> removeCategoryIds = CollectionUtils.subtract(oldCategoryIds, newCategoryIds);

        // 课程分类关系删除
        if (!removeCategoryIds.isEmpty()) {
            List<Long> relationIds = courseCategories.stream().filter(r -> removeCategoryIds.contains(r.getCategoryId())).map(CourseCategoryDTO::getId).collect(Collectors.toList());
            this.courseCategoryService.deleteByIds(relationIds);
        }

        // 差集，需要新增的分类
        Collection<Long> addCategoryIds = CollectionUtils.subtract(newCategoryIds, oldCategoryIds);
        for (Long categoryId : addCategoryIds) {
            this.saveCourseCategory(courseId, categoryId);
        }

        // 先移除讲师
        this.courseLecturerService.remove(Wrappers.<CourseLecturer>lambdaQuery().eq(CourseLecturer::getCourseId, courseId));
        // 再保存讲师
        params.getLecturers().forEach(courseLecturer -> {
            courseLecturer.setCourseId(courseId);
            courseLecturer.setId(null);
            courseLecturer.setOrgId(null);
        });
        this.courseLecturerService.saveBatch(params.getLecturers());

        // 保存可见用户
        //if (UserVisibleStatusEnum.ALL.getCode().equals(params.getUserVisibleStatus())) {
        //    this.courseUserVisibleService.deleteByCourseId(courseId);
        //} else {
        //    List<CourseUserVisible> newUsers = params.getVisibleUsers();
        //    if (CollUtil.isNotEmpty(newUsers)) {
        //        List<CourseUserVisible> oldUsers = this.courseUserVisibleService.getByCourseId(courseId);
        //        if (oldUsers.isEmpty()) {
        //            newUsers.forEach(user -> {
        //                user.setCourseId(courseId);
        //                user.setId(null);
        //            });
        //            this.courseUserVisibleService.saveBatch(newUsers);
        //        } else {
        //            List<Long> oldUserIds = oldUsers.stream().map(CourseUserVisible::getUserId).collect(Collectors.toList());
        //            List<CourseUserVisible> adds = newUsers.stream().filter(e -> !oldUserIds.contains(e.getUserId())).collect(Collectors.toList());
        //            if (!adds.isEmpty()) {
        //                adds.forEach(user -> {
        //                    user.setCourseId(courseId);
        //                    user.setId(null);
        //                });
        //                this.courseUserVisibleService.saveBatch(adds);
        //            }
        //        }
        //    }
        //}
        //
        ////先删除部门
        //this.courseUserDeptVisibleService.deleteByCourseId(courseId);
        ////再保存部门
        //if(params.getCourseUserDeptVisibles() != null){
        //    List<CourseUserDeptVisible> newDepts = params.getCourseUserDeptVisibles();
        //    newDepts.forEach(dept -> {
        //        dept.setCourseId(courseId);
        //        dept.setId(null);
        //    });
        //    this.courseUserDeptVisibleService.saveBatch(newDepts);
        //}
        //
        ////删除可见标签
        //this.courseUserGroupVisibleService.deleteByCourseId(courseId);
        ////保存可见标签
        //if(params.getCourseUserGroupVisibles() != null){
        //    List<CourseUserGroupVisible> newGroups = params.getCourseUserGroupVisibles();
        //    newGroups.forEach(group -> {
        //        group.setCourseId(courseId);
        //        group.setId(null);
        //    });
        //    this.courseUserGroupVisibleService.saveBatch(newGroups);
        //}

        CourseInfo courseInfo = new CourseInfo();
        BeanUtils.copyProperties(params, courseInfo, "id");
        courseInfo.setCourseId(courseId);
        this.courseInfoService.updateByCourseId(courseInfo);

        BeanUtils.copyProperties(params, course);
        return this.updateById(course);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CourseCategory saveCourseCategory(Long courseId, Long categoryId) {
        // 保存课程分类
        CourseCategory courseCategory = new CourseCategory();
        courseCategory.setCourseId(courseId);
        courseCategory.setCategoryId(categoryId);
        this.courseCategoryService.save(courseCategory);
        // 课程分类包含课程数 +1
        //this.categoryDistributionService.incrCourseCount(categoryId, 1);
        return courseCategory;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByIds(List<Long> idList) {
        for (Long id:idList) {
            ClazzPageRequestDTO clazzPageRequestDTO=new ClazzPageRequestDTO();
            clazzPageRequestDTO.setOrgId(SecurityContextHolder.getOrgId());
            clazzPageRequestDTO.setCourseId(id);
            Long count=remoteService.getCountByCourseId(clazzPageRequestDTO);
            if(count>Constants.DB_FAIL){
                Course course=this.getById(id);
                throw new ServiceException(course.getCourseName()+"已被班级使用，请先删除相关班级再删除课程");
            }
        }
        boolean remove = this.removeByIds(idList);
        if (remove) {
            this.dealAssociation(idList);
        }
        return remove;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteLogicByIds(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            return false;
        }
        List<Course> logicDelCourses = idList.stream().map(id -> {
            Course course = new Course();
            course.setId(id);
            course.setDeleted(1);
            return course;
        }).collect(Collectors.toList());
        boolean delete = this.updateBatchById(logicDelCourses);
        if (delete) {
            this.dealAssociation(idList);
        }
        return delete;
    }

    /**
     * 处理管理数据
     *
     * @param idList 课程ids
     */
    private void dealAssociation(List<Long> idList) {
        // 先查询课程下所属分类
        List<CourseCategory> list = this.courseCategoryService.lambdaQuery().in(CourseCategory::getCourseId, idList).list();
        this.courseCategoryService.deleteByIds(list.stream().map(CourseCategory::getId).collect(Collectors.toList()));
        //先查询课程与讲师关联关系
        List<CourseLecturer> list2 = this.courseLecturerService.lambdaQuery().in(CourseLecturer::getCourseId, idList).eq(CourseLecturer::getOrgId,SecurityContextHolder.getOrgId()).list();
        this.courseLecturerService.deleteByIds(list2.stream().map(CourseLecturer::getId).collect(Collectors.toList()));
        //先查询课程预报学员关系
        List<TCourseStudent> list3 = this.tCourseStudentService.lambdaQuery().in(TCourseStudent::getCourseId, idList).eq(TCourseStudent::getOrgId,SecurityContextHolder.getOrgId()).list();
        this.tCourseStudentService.removeByIds(list3.stream().map(TCourseStudent::getId).collect(Collectors.toList()));
        // 分组统计
        //Map<Long, Integer> categoryCount = list.stream().collect(Collectors.groupingBy(CourseCategory::getCategoryId, Collectors.reducing(0, e -> 1, Integer::sum)));
        // 分类下的课程数量减一
        //categoryCount.forEach((categoryId, count) -> this.categoryDistributionService.decrCourseCount(categoryId, count));
        // 删除课程关联的直播
        //courseLiveLinkService.deleteByCourseIds(idList);
        //List<CourseComponent> liveList = courseComponentService.lambdaQuery().in(CourseComponent::getCourseId, idList).eq(CourseComponent::getComponentTypeCode, ComponentTypeEnum.ZB.getCode()).list();
        //for (CourseComponent component : liveList) {
        //    if (ComponentTypeEnum.ZB.getCode().equals(component.getComponentTypeCode())) {
        //        remoteService.remoteAssociateLive(component.getId(), 2);
        //    }
        //}
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Course copy(Long courseId, String courseName) {
        Course source = this.getById(courseId);
        if (source == null) {
            throw new ServiceException("该课程不存在，请刷新重试！");
        }
        if (StrUtil.isNotBlank(courseName)) {
            source.setCourseName(courseName);
        }
        // 构造 CourseAddRequest 对象
        CourseAddRequest addRequest = new CourseAddRequest();
        BeanUtils.copyProperties(source, addRequest);
        // 去掉 orgId
        addRequest.setOrgId(null);

        CourseInfo courseInfo = this.courseInfoService.getByCourseId(courseId);
        addRequest.setUserVisibleStatus(courseInfo.getUserVisibleStatus());
        addRequest.setPlaybackSpeed(courseInfo.getPlaybackSpeed());
        addRequest.setPlaybackDrag(courseInfo.getPlaybackDrag());
        addRequest.setCourseContent(courseInfo.getCourseContent());
        addRequest.setCoursePoints(courseInfo.getCoursePoints());

        // 可见用户
        if (UserVisibleStatusEnum.SOME.getCode().equals(courseInfo.getUserVisibleStatus())) {
            addRequest.setVisibleUsers(this.courseUserVisibleService.listByCourseId(courseId));
        }

        // 讲师
        addRequest.setLecturers(this.courseLecturerService.listByCourseId(courseId));

        List<CourseCategoryDTO> courseCategories = this.courseCategoryService.listByCourseId(courseId);
        if (!courseCategories.isEmpty()) {
            addRequest.setCategoryIds(CollStreamUtil.toSet(courseCategories, CourseCategoryDTO::getCategoryId));
        }
        // 新增课程基本信息
        Course course = this.add(addRequest);

        // 2022-02-18 由于直播只能关联一个课程，所以课程类型是直播或者综合课程，则不进行内容的复制
        if (CourseTypeEnum.RECORD.getCode().equals(source.getCourseType())) {
            // 新增课程内容
            CourseContentResponse detailContent = this.detailContent(courseId);
            if (detailContent != null && CollUtil.isNotEmpty(detailContent.getChapterList())) {
                Long id = course.getId();
                detailContent.getChapterList().stream().map(c -> {
                    CourseChapterAddRequest chapterAddRequest = BeanUtil.copyProperties(c, CourseChapterAddRequest.class);
                    if (CollUtil.isNotEmpty(c.getLessonList())) {
                        List<CourseLessonAddRequest> lessonAddRequestList = c.getLessonList().stream().map(l -> {
                            CourseLessonAddRequest lessonAddRequest = BeanUtil.copyProperties(l, CourseLessonAddRequest.class);
                            if (CollUtil.isNotEmpty(l.getComponentList())) {
                                List<CourseComponentAddRequest> componentAddRequests = BeanUtil.copyToList(l.getComponentList(), CourseComponentAddRequest.class);
                                lessonAddRequest.setComponentList(componentAddRequests);
                            }
                            return lessonAddRequest;
                        }).collect(Collectors.toList());
                        chapterAddRequest.setLessonList(lessonAddRequestList);
                    }
                    return chapterAddRequest;
                }).forEach(chapter -> this.saveChapter(chapter, id));
            }
        }
        return course;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Course addContent(CourseContentAddRequest params) {
        Long courseId = params.getCourseId();
        Course course = this.getById(courseId);
        if (course == null) {
            throw new ServiceException("该课程不存在，请刷新重试！");
        }
        params.getChapterList().forEach(chapter -> this.saveChapter(chapter, courseId));
        return course;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateContent(CourseContentUpdateRequest params) {
        Long courseId = params.getCourseId();
        Course course = this.getById(courseId);
        if (course == null) {
            throw new ServiceException("该课程不存在，请刷新重试！");
        }
        // 先删除
        this.delChapterByCourseId(courseId);
        // 再新增
        params.getChapterList().forEach(chapter -> this.saveChapter(chapter, courseId));
        return Boolean.TRUE;
    }

    @Override
    public CourseContentResponse detailContent(Long courseId) {
        CourseContentResponse courseContentResponse = new CourseContentResponse();
        courseContentResponse.setCourseId(courseId);
        // 查询课程信息
        //Course course = this.getById(courseId);
        //Optional.ofNullable(course).ifPresent(c -> {
        //    courseContentResponse.setCourseName(c.getCourseName());
        //    CourseInfo courseInfo = this.courseInfoService.getByCourseId(courseId);
        //    if (courseInfo != null) {
        //        courseContentResponse.setPlaybackSpeed(courseInfo.getPlaybackSpeed());
        //        courseContentResponse.setCourseMode(courseInfo.getCourseMode());
        //    }
        //    List<CourseChapterDTO> chapterList = this.baseMapper.selectContentById(courseId);
        //    for (CourseChapterDTO courseChapterDTO : chapterList) {
        //        for (CourseLessonDTO courseLessonDTO : courseChapterDTO.getLessonList()) {
        //            for (CourseComponentDTO componentDTO : courseLessonDTO.getComponentList()) {
        //                if (ComponentTypeEnum.ZB.getCode().equals(componentDTO.getComponentTypeCode())) {
        //                    // 直播课程需要加入直播时间
        //                    LiveResultDTO liveResultDTO = remoteService.remoteLive(componentDTO.getResourceFileId());
        //                    if (liveResultDTO != null) {
        //                        BeanUtils.copyProperties(liveResultDTO, componentDTO, "id", "sort");
        //                    }
        //                }
        //            }
        //            if (CollUtil.isNotEmpty(courseLessonDTO.getComponentList()) && courseLessonDTO.getComponentList().size() > 1) {
        //                List<CourseComponentDTO> componentSorts = courseLessonDTO.getComponentList().stream()
        //                        .sorted(Comparator.comparing(CourseComponentDTO::getSort).thenComparing(CourseComponentDTO::getComponentId))
        //                        .collect(Collectors.toList());
        //                courseLessonDTO.setComponentList(componentSorts);
        //            }
        //        }
        //        if (CollUtil.isNotEmpty(courseChapterDTO.getLessonList()) && courseChapterDTO.getLessonList().size() > 1) {
        //            List<CourseLessonDTO> lessonSorts = courseChapterDTO.getLessonList().stream()
        //                    .sorted(Comparator.comparing(CourseLessonDTO::getSort).thenComparing(CourseLessonDTO::getLessonId))
        //                    .collect(Collectors.toList());
        //            courseChapterDTO.setLessonList(lessonSorts);
        //        }
        //    }
        //    if (CollUtil.isNotEmpty(chapterList) && chapterList.size() > 1) {
        //        chapterList = chapterList.stream().sorted(Comparator.comparing(CourseChapterDTO::getSort).thenComparing(CourseChapterDTO::getChapterId)).collect(Collectors.toList());
        //    }
        //    courseContentResponse.setChapterList(chapterList);
        //});
        return courseContentResponse;
    }

    @Override
    public CourseContentResponse shopDetailContent(Long courseId) {
        CourseContentResponse courseContentResponse = new CourseContentResponse();
        courseContentResponse.setCourseId(courseId);
        //// 查询课程信息
        //Course course = this.baseMapper.shopGetCourseById(courseId);
        //Optional.ofNullable(course).ifPresent(c -> {
        //    courseContentResponse.setCourseName(c.getCourseName());
        //    CourseInfo courseInfo = this.courseInfoService.getByCourseId(courseId);
        //    if (courseInfo != null) {
        //        courseContentResponse.setPlaybackSpeed(courseInfo.getPlaybackSpeed());
        //        courseContentResponse.setCourseMode(courseInfo.getCourseMode());
        //    }
        //    List<CourseChapterDTO> chapterList = this.baseMapper.selectContentById(courseId);
        //    for (CourseChapterDTO courseChapterDTO : chapterList) {
        //        for (CourseLessonDTO courseLessonDTO : courseChapterDTO.getLessonList()) {
        //            for (CourseComponentDTO componentDTO : courseLessonDTO.getComponentList()) {
        //                if (ComponentTypeEnum.ZB.getCode().equals(componentDTO.getComponentTypeCode())) {
        //                    // 直播课程需要加入直播时间
        //                    LiveResultDTO liveResultDTO = remoteService.shopRemoteLive(componentDTO.getResourceFileId(), courseInfo.getOrgId());
        //                    if (liveResultDTO != null) {
        //                        BeanUtils.copyProperties(liveResultDTO, componentDTO, "id", "sort");
        //                    }
        //                }
        //            }
        //            if (CollUtil.isNotEmpty(courseLessonDTO.getComponentList()) && courseLessonDTO.getComponentList().size() > 1) {
        //                List<CourseComponentDTO> componentSorts = courseLessonDTO.getComponentList().stream()
        //                        .sorted(Comparator.comparing(CourseComponentDTO::getSort).thenComparing(CourseComponentDTO::getComponentId))
        //                        .collect(Collectors.toList());
        //                courseLessonDTO.setComponentList(componentSorts);
        //            }
        //        }
        //        if (CollUtil.isNotEmpty(courseChapterDTO.getLessonList()) && courseChapterDTO.getLessonList().size() > 1) {
        //            List<CourseLessonDTO> lessonSorts = courseChapterDTO.getLessonList().stream()
        //                    .sorted(Comparator.comparing(CourseLessonDTO::getSort).thenComparing(CourseLessonDTO::getLessonId))
        //                    .collect(Collectors.toList());
        //            courseChapterDTO.setLessonList(lessonSorts);
        //        }
        //    }
        //    if (CollUtil.isNotEmpty(chapterList) && chapterList.size() > 1) {
        //        chapterList = chapterList.stream().sorted(Comparator.comparing(CourseChapterDTO::getSort).thenComparing(CourseChapterDTO::getChapterId)).collect(Collectors.toList());
        //    }
        //    courseContentResponse.setChapterList(chapterList);
        //});
        return courseContentResponse;
    }

    /**
     * 保存课程章节内容
     *
     * @param chapterParam 参数
     * @param courseId     课程ID
     */
    private void saveChapter(CourseChapterAddRequest chapterParam, Long courseId) {
        List<CourseLessonAddRequest> lessonList = chapterParam.getLessonList();
        CourseChapter chapter = new CourseChapter();
        BeanUtils.copyProperties(chapterParam, chapter);
        chapter.setCourseId(courseId);
        boolean isNotNull = Optional.ofNullable(lessonList).isPresent();
        chapter.setLessonNum(isNotNull ? lessonList.size() : 0);
        // 保存章节
        this.courseChapterService.save(chapter);
        if (isNotNull) {
            Long chapterId = chapter.getId();
            // 保存讲次
            for (CourseLessonAddRequest lesson : lessonList) {
                CourseLesson courseLesson = new CourseLesson();
                BeanUtils.copyProperties(lesson, courseLesson);
                courseLesson.setChapterId(chapterId);
                courseLesson.setCourseId(courseId);
                this.courseLessonService.save(courseLesson);
                Long lessonId = courseLesson.getId();
                for (CourseComponentAddRequest componentAddRequest : lesson.getComponentList()) {
                    CourseComponent component = new CourseComponent();
                    component.setCourseId(courseId);
                    component.setLessonId(lessonId);
                    BeanUtils.copyProperties(componentAddRequest, component);
                    this.courseComponentService.save(component);
                    Long componentId = component.getId();
                    CourseComponentAttachment componentAttachment = new CourseComponentAttachment();
                    BeanUtils.copyProperties(componentAddRequest, componentAttachment);
                    componentAttachment.setComponentId(componentId);
                    componentAttachment.setCourseId(courseId);
                    componentAttachment.setLessonId(lessonId);
                    this.courseComponentAttachmentService.save(componentAttachment);
                }

            }
        }
    }

    private void delChapterByCourseId(Long courseId) {
        this.courseChapterService.deleteByCourseId(courseId);
        this.courseLessonService.deleteByCourseId(courseId);
        this.courseComponentService.deleteByCourseId(courseId);
        this.courseComponentAttachmentService.deleteByCourseId(courseId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Course copyToOrg(Long courseId, String courseName, Long toOrg) {
        //TODO 如果该机构没有分类的情况下，需要自动创建一个拷贝过来的课程分类？
        return this.copy(courseId, courseName);
    }

    @Override
    public IPage<CoursePageViewDTO> pageCenter(CoursePageSearchDTO params) {
        if (params.getCategoryId() != null) {
            Set<Long> categoryIds = params.getCategoryIds();
            if (CollUtil.isEmpty(categoryIds)) {
                categoryIds = new HashSet<>();
            }
            categoryIds.add(params.getCategoryId());
            params.setCategoryIds(categoryIds);
            params.setCategoryId(null);
        }
        IPage<CoursePageViewDTO> page;
        Page<CoursePageViewDTO> mpPage = params.createMpPage();
        mpPage.addOrder(new OrderItem("id", false));

        // 查询学员的标签和部门
        StudentRelationDTO studentRelation = remoteService.getStudentRelation(SecurityContextHolder.getStudentId());
        params.setDeptIds(studentRelation.getDeptIds());
        params.setGroupIds(studentRelation.getGroupIds());

        if (Boolean.TRUE.equals(params.getInner())) {
            if (params.getIsSignUp() != null) {
                List<CourseStudent> courseList = this.courseStudentService.listCourseByUserId(params.getUserId());
                if (courseList.isEmpty()) {
                    return mpPage;
                }
                params.setCourseIds(courseList.stream().map(CourseStudent::getCourseId).distinct().collect(Collectors.toList()));
                page = this.baseMapper.pageInnerCourseSignUp(mpPage, params);
            } else {
                // 内训，展示设置为部分可见的课程以及所有可见的课程
                page = this.baseMapper.pageInnerCourse(mpPage, params);
            }
        } else {
            // 外训，是否需要一个对外发布的状态？
            page = this.baseMapper.pagePublishCourse(mpPage, params);
        }
//        if (StringUtils.isNotBlank(params.getSortField()) && params.getSortField().equals("sign_up_num")){
//            //根据报名人数降序排列数据（解决设置虚拟报名人数后根据报名人数排序不准确的问题）
//            Comparator<CoursePageViewDTO> comparator = (t1, t2) -> t1.getSignUpNum().compareTo(t2.getSignUpNum());
//            page.getRecords().sort(comparator.reversed());
//        }
        return page;
    }

    /**
     * 1.总共推荐5个课程；
     * 2.推荐前三个课程为同个讲师的三个报名最多的课程，若不足三个则推荐最新的直播课；
     * 3.后两个课程为报名最多的课程。
     */
    @Override
    public List<CoursePageViewDTO> recommend(CourseListSearchDTO params) {
        int lecturerCount = 3;
        int hotCount = 2;
        List<CoursePageViewDTO> recommendList = new ArrayList<>(lecturerCount + hotCount);
        CoursePageSearchDTO coursePage = new CoursePageSearchDTO();
        coursePage.setSortField("create_time");
        coursePage.setSortOrder("desc");
        //查询学员的标签和部门
        StudentRelationDTO studentRelation = remoteService.getStudentRelation(SecurityContextHolder.getStudentId());
        params.setDeptIds(studentRelation.getDeptIds());
        params.setGroupIds(studentRelation.getGroupIds());
        coursePage.setDeptIds(studentRelation.getDeptIds());
        coursePage.setGroupIds(studentRelation.getGroupIds());

        if (CollUtil.isEmpty(params.getLecturerIds())) {
            // 查询3门最新的直播课程
            coursePage.setCourseType(CourseTypeEnum.LIVE.getCode());
            coursePage.setSize(lecturerCount);
            IPage<CoursePageViewDTO> page = this.baseMapper.pageInnerCourse(coursePage.createMpPage().setSearchCount(false), coursePage);
            if (!page.getRecords().isEmpty()) {
                recommendList.addAll(page.getRecords());
            }
        } else {
            // 推荐前三个课程为同个讲师的三个报名最多的课程，若不足三个则推荐最新的直播课；
            coursePage.setSortField("sign_up_num");
            coursePage.setSize(lecturerCount);
            IPage<CoursePageViewDTO> page = this.baseMapper.pageRecommendCourse(coursePage.createMpPage().setSearchCount(false), params);
            if (!page.getRecords().isEmpty()) {
                recommendList.addAll(page.getRecords());
            }
            int leftCount = lecturerCount - recommendList.size();
            if (leftCount > 0) {
                coursePage.setCourseType(CourseTypeEnum.LIVE.getCode());
                coursePage.setSortField("create_time");
                coursePage.setSize(leftCount);
                if (!recommendList.isEmpty()) {
                    // 排除已有课程，避免重复
                    coursePage.setIgnoreCourseIds(recommendList.stream().map(CoursePageViewDTO::getId).collect(Collectors.toList()));
                }
                page = this.baseMapper.pageInnerCourse(coursePage.createMpPage().setSearchCount(false), coursePage);
                if (!page.getRecords().isEmpty()) {
                    recommendList.addAll(page.getRecords());
                }
            }
        }
        // 2门课程为报名最多的课程
        coursePage.setCourseType(null);
        coursePage.setSortField("sign_up_num");
        coursePage.setSize(lecturerCount - recommendList.size() + hotCount);
        if (!recommendList.isEmpty()) {
            // 排除已有课程，避免重复
            coursePage.setIgnoreCourseIds(recommendList.stream().map(CoursePageViewDTO::getId).collect(Collectors.toList()));
        }
        IPage<CoursePageViewDTO> page = this.baseMapper.pageInnerCourse(coursePage.createMpPage().setSearchCount(false), coursePage);
        if (!page.getRecords().isEmpty()) {
            recommendList.addAll(page.getRecords());
        }
        return recommendList;
    }

    @Override
    public IPage<CoursePageViewDTO> pageCourseByIndex(CoursePageIndexRequest params) {
        if(params.getStudentId()==null){
            return this.baseMapper.pageCourseByIndex(params.createMpPage(), params);
        }
        // 查询学员的标签和部门
        StudentRelationDTO studentRelation = remoteService.getStudentRelation(params.getStudentId());
        params.setDeptIds(studentRelation.getDeptIds());
        params.setGroupIds(studentRelation.getGroupIds());
        return this.baseMapper.pageInnerCourseByIndex(params.createMpPage(), params);
    }

    @Override
    public IPage<CoursePageByChildResponse> pageByChild(CoursePageRequest params) {
        if(params.getStudentId()==null){
            return this.baseMapper.pageByChild(params.createMpPage(),params);
        }
        // 查询学员的标签和部门
        StudentRelationDTO studentRelation = remoteService.getStudentRelation(params.getStudentId());
        params.setDeptIds(studentRelation.getDeptIds());
        params.setGroupIds(studentRelation.getGroupIds());
        return this.baseMapper.pageInnerByChild(params.createMpPage(), params);
    }

    @Override
    public IPage<CoursePageResponse> pageAdmin(CoursePageRequest params) {
        IPage<CoursePageResponse> page = this.baseMapper.pageAdmin(params.createMpPage(), params);
        return page;
    }

    @Override
    public List<CourseListResponse> listSelect(CourseListRequest params) {
        return this.baseMapper.listSelect(params);
    }

    @Override
    public List<CourseListResponse> shopListSelect(CourseListRequest params) {
        return this.baseMapper.shopListSelect(params);
    }

    @Override
    public Boolean sortContentTree(CourseContentSortRequest request) {
        LevelEnum level = LevelEnum.getEnumByCode(request.getLevel());
        if (level == null) {
            return Boolean.FALSE;
        }
        List<Long> sortedIds = request.getSortedIds();
        AtomicInteger serialNum = new AtomicInteger(1);
        switch (level) {
            case FIRST:
                // 调整章顺序
                List<CourseChapter> chapters = courseChapterService.listByCourseId(request.getCourseId());
                if (chapters.isEmpty() || chapters.size() != sortedIds.size()) {
                    throw new ServiceException("章数据有误");
                }
                List<Long> chapterIds = chapters.stream().map(CourseChapter::getId).collect(Collectors.toList());
                // id内容不一致
                if (chapterIds.retainAll(sortedIds)) {
                    throw new ServiceException("章数据有误");
                }
                List<CourseChapter> chapterList = sortedIds.stream().distinct().map(i -> {
                    CourseChapter chapter = new CourseChapter();
                    chapter.setId(i);
                    chapter.setSort(serialNum.getAndIncrement());
                    return chapter;
                }).collect(Collectors.toList());
                courseChapterService.updateBatchById(chapterList);
                break;
            case SECOND:
                // 调整节顺序
                Long chapterId = request.getChapterId();
                if (chapterId == null) {
                    throw new ServiceException("章ID不允许为空");
                }
                List<CourseLesson> lessons = courseLessonService.listByChapterId(chapterId);
                if (lessons.isEmpty() || lessons.size() != sortedIds.size()) {
                    throw new ServiceException("节数据有误");
                }
                List<Long> lessonIds = lessons.stream().map(CourseLesson::getId).collect(Collectors.toList());
                // id内容不一致
                if (lessonIds.retainAll(sortedIds)) {
                    throw new ServiceException("节数据有误");
                }
                List<CourseLesson> lessonList = sortedIds.stream().distinct().map(i -> {
                    CourseLesson lesson = new CourseLesson();
                    lesson.setId(i);
                    lesson.setSort(serialNum.getAndIncrement());
                    return lesson;
                }).collect(Collectors.toList());
                courseLessonService.updateBatchById(lessonList);
                break;
            case THIRD:
                // 调整内容顺序
                Long lessonId = request.getLessonId();
                if (lessonId == null) {
                    throw new ServiceException("节ID不允许为空");
                }
                List<CourseComponent> components = courseComponentService.listByLessonId(lessonId);
                if (components.isEmpty() || components.size() != sortedIds.size()) {
                    throw new ServiceException("内容数据有误");
                }
                List<Long> currIds = components.stream().map(CourseComponent::getId).collect(Collectors.toList());
                // id内容不一致
                if (currIds.retainAll(sortedIds)) {
                    throw new ServiceException("内容数据有误");
                }
                List<CourseComponent> componentList = sortedIds.stream().distinct().map(i -> {
                    CourseComponent component = new CourseComponent();
                    component.setId(i);
                    component.setSort(serialNum.getAndIncrement());
                    return component;
                }).collect(Collectors.toList());
                courseComponentService.updateBatchById(componentList);
                break;
            default:
                throw new ServiceException("层级类型有误");
        }
        return Boolean.TRUE;
    }

    @Override
    public CourseContentResponse contents(Long courseId) {
        CourseContentResponse courseContentResponse = new CourseContentResponse();
        courseContentResponse.setCourseId(courseId);
        // 查询课程信息
        Course course = this.getById(courseId);
        Optional.ofNullable(course).ifPresent(c -> {
            courseContentResponse.setCourseName(c.getCourseName());
            CourseInfo courseInfo = this.courseInfoService.getByCourseId(courseId);
            if (courseInfo != null) {
                courseContentResponse.setCourseMode(courseInfo.getCourseMode());
            }
            List<CourseChapterDTO> chapterList = this.baseMapper.selectCatalogById(courseId);
            for (CourseChapterDTO courseChapterDTO : chapterList) {
                if (CollUtil.isNotEmpty(courseChapterDTO.getLessonList()) && courseChapterDTO.getLessonList().size() > 1) {
                    List<CourseLessonDTO> lessonSorts = courseChapterDTO.getLessonList().stream()
                            .sorted(Comparator.comparing(CourseLessonDTO::getSort).thenComparing(CourseLessonDTO::getLessonId))
                            .collect(Collectors.toList());
                    courseChapterDTO.setLessonList(lessonSorts);
                }
            }
            if (CollUtil.isNotEmpty(chapterList) && chapterList.size() > 1) {
                chapterList = chapterList.stream().sorted(Comparator.comparing(CourseChapterDTO::getSort).thenComparing(CourseChapterDTO::getChapterId)).collect(Collectors.toList());
            }
            courseContentResponse.setChapterList(chapterList);
        });
        return courseContentResponse;
    }

    @Override
    public Long categoryCount(List<Long> categoryIdList, Long lecturerId, Long studentId) {
        return this.courseCategoryMapper.categoryCount(categoryIdList, lecturerId, studentId);
    }

    @Override
    public Long groupCount(List<Long> groupIdList) {
        return this.baseMapper.groupCount(groupIdList);
    }

    @Override
    public Long deptCount(List<Long> deptIdList) {
        return this.baseMapper.deptCount(deptIdList);
    }

    @Override
    public IPage<CoursePageResponse> shopCoursePage(CoursePageRequest params) {
        IPage<CoursePageResponse> page = null;
        if (StrUtil.isNotBlank(params.getLecturerName())) {
            // 讲师名称搜索
            page = this.baseMapper.shopCoursePageByLecturer(params.createMpPage(), params);
        } else {
            page = this.baseMapper.shopCoursePage(params.createMpPage(), params);

        }
        //List<Long> ids = page.getRecords().stream().map(CoursePageResponse::getCreateBy).distinct().collect(Collectors.toList());
        //if (!ids.isEmpty()) {
        //    // 远程调用获取用户信息
        //    List<UserListResultDTO> userList = this.remoteService.remoteUserList(ids);
        //    if (CollUtil.isNotEmpty(userList)) {
        //        Map<Long, String> userMap = userList.stream().collect(Collectors.toMap(UserListResultDTO::getId, UserListResultDTO::getRealName, (o1, o2) -> o1));
        //        for (CoursePageResponse course : page.getRecords()) {
        //            course.setRealName(userMap.get(course.getCreateBy()));
        //        }
        //    }
        //}
        // 获取课程讲师
        for (CoursePageResponse record : page.getRecords()) {
            record.setLecturers(courseLecturerService.listByCourseId(record.getId()));
        }
        return page;
    }
    @Override
    public IPage<CoursePageResponse> getOrgList(CoursePageRequest params) {
        params.setEnabled(Constants.DB_TRUE);
        return baseMapper.page(params.createMpPage(),params);
    }
}

