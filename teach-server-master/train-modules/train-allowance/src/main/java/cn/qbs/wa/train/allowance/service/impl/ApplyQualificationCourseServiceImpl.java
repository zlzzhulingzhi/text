package cn.qbs.wa.train.allowance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.train.allowance.entity.ApplyQualificationCourse;
import cn.qbs.wa.train.allowance.mapper.ApplyQualificationCourseMapper;
import cn.qbs.wa.train.allowance.pojo.apply.QualificationCourseRequest;
import cn.qbs.wa.train.allowance.service.ApplyQualificationCourseService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 资助资格申请-项目计划-课程(ApplyQualificationCourse)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2022-11-02 11:20:28
 */
@Slf4j
@Service("applyQualificationCourseService")
public class ApplyQualificationCourseServiceImpl extends ServiceImpl<ApplyQualificationCourseMapper, ApplyQualificationCourse> implements ApplyQualificationCourseService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateByProjectId(Long projectId, List<QualificationCourseRequest> requestList) {
        Long orgId = SecurityContextHolder.getOrgId();
        // 移除项目下关联的课程
        this.remove(Wrappers.<ApplyQualificationCourse>lambdaQuery().eq(ApplyQualificationCourse::getProjectId, projectId).eq(ApplyQualificationCourse::getOrgId, orgId));

        this.addByProjectId(projectId, requestList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addByProjectId(Long projectId, List<QualificationCourseRequest> requestList) {
        Long orgId = SecurityContextHolder.getOrgId();
        List<ApplyQualificationCourse> courseList = requestList.stream().map(c -> {
            ApplyQualificationCourse course = BeanUtil.copyProperties(c, ApplyQualificationCourse.class);
            course.setProjectId(projectId);
            course.setOrgId(orgId);
            return course;
        }).collect(Collectors.toList());
        this.saveBatch(courseList);
    }
}

