package cn.qbs.wa.train.allowance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.train.allowance.entity.ApplyQualificationCourse;
import cn.qbs.wa.train.allowance.entity.ApplyQualificationProject;
import cn.qbs.wa.train.allowance.mapper.ApplyQualificationProjectMapper;
import cn.qbs.wa.train.allowance.pojo.apply.QualificationProjectAddRequest;
import cn.qbs.wa.train.allowance.pojo.apply.QualificationProjectRequest;
import cn.qbs.wa.train.allowance.pojo.apply.QualificationProjectResponse;
import cn.qbs.wa.train.allowance.pojo.apply.QualificationProjectUpdateRequest;
import cn.qbs.wa.train.allowance.service.ApplyQualificationCourseService;
import cn.qbs.wa.train.allowance.service.ApplyQualificationProjectService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 资助资格申请-项目计划(ApplyQualificationProject)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2022-11-02 11:20:28
 */
@Slf4j
@Service("applyQualificationProjectService")
public class ApplyQualificationProjectServiceImpl extends ServiceImpl<ApplyQualificationProjectMapper, ApplyQualificationProject> implements ApplyQualificationProjectService {

    @Resource
    private ApplyQualificationCourseService qualificationCourseService;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public ApplyQualificationProject add(QualificationProjectAddRequest p) {
        Long orgId = SecurityContextHolder.getOrgId();
        ApplyQualificationProject project = BeanUtil.copyProperties(p, ApplyQualificationProject.class);
        project.setOrgId(orgId);
        this.save(project);
        // 保存项目计划下的课程信息
        if (CollUtil.isNotEmpty(p.getCourseList())) {
            qualificationCourseService.addByProjectId(project.getId(), p.getCourseList());
        }
        return project;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean batchAdd(List<QualificationProjectRequest> projectList, Long qualificationId) {
        if (CollUtil.isNotEmpty(projectList)) {
            // 资质申请表单ID
            for (QualificationProjectRequest p : projectList) {
                QualificationProjectAddRequest project = BeanUtil.copyProperties(p, QualificationProjectAddRequest.class);
                project.setQualificationId(qualificationId);
                this.add(project);
            }
        }
        return Boolean.TRUE;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public Boolean update(QualificationProjectUpdateRequest p) {
        Long orgId = SecurityContextHolder.getOrgId();
        Long projectId = p.getProjectId();
        ApplyQualificationProject old = this.getById(projectId);
        if (old == null) {
            throw new ServiceException("数据不存在");
        }
        if (!old.getOrgId().equals(orgId)) {
            throw new ServiceException("非法操作");
        }
        ApplyQualificationProject project = BeanUtil.copyProperties(p, ApplyQualificationProject.class);
        project.setId(projectId);
        boolean update = this.updateById(project);
        qualificationCourseService.updateByProjectId(projectId, p.getCourseList());
        return update;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean batchUpdate(List<QualificationProjectRequest> projectList, Long qualificationId) {
        // 先删除
        this.delByQualId(qualificationId);
        // 后新增
        return this.batchAdd(projectList, qualificationId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean remove(Long projectId) {
        Long orgId = SecurityContextHolder.getOrgId();
        // 移除项目下关联的课程
        qualificationCourseService.remove(Wrappers.<ApplyQualificationCourse>lambdaQuery().eq(ApplyQualificationCourse::getProjectId, projectId).eq(ApplyQualificationCourse::getOrgId, orgId));
        // 移除项目
        return this.remove(Wrappers.<ApplyQualificationProject>lambdaQuery().eq(ApplyQualificationProject::getId, projectId).eq(ApplyQualificationProject::getOrgId, orgId));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delByQualId(Long qualificationId) {
        Long orgId = SecurityContextHolder.getOrgId();
        List<ApplyQualificationProject> projectList = this.lambdaQuery().select(ApplyQualificationProject::getId).eq(ApplyQualificationProject::getQualificationId, qualificationId).eq(ApplyQualificationProject::getOrgId, orgId).list();
        if (CollUtil.isNotEmpty(projectList)) {
            List<Long> projectIds = CollStreamUtil.toList(projectList, ApplyQualificationProject::getId);
            // 移除项目下关联的课程
            qualificationCourseService.remove(Wrappers.<ApplyQualificationCourse>lambdaQuery().in(ApplyQualificationCourse::getProjectId, projectIds).eq(ApplyQualificationCourse::getOrgId, orgId));
            // 移除项目
            this.removeByIds(projectIds);
        }
    }

    @Override
    public List<QualificationProjectResponse> listProjectInfoByQualId(Long qualificationId) {
        return this.baseMapper.listProjectInfoByQualId(qualificationId);
    }
}

