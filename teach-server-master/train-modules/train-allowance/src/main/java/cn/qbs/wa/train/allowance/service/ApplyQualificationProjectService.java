package cn.qbs.wa.train.allowance.service;

import cn.qbs.wa.train.allowance.entity.ApplyQualificationProject;
import cn.qbs.wa.train.allowance.pojo.apply.QualificationProjectAddRequest;
import cn.qbs.wa.train.allowance.pojo.apply.QualificationProjectRequest;
import cn.qbs.wa.train.allowance.pojo.apply.QualificationProjectResponse;
import cn.qbs.wa.train.allowance.pojo.apply.QualificationProjectUpdateRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 资助资格申请-项目计划(ApplyQualificationProject)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2022-11-02 11:20:28
 */
public interface ApplyQualificationProjectService extends IService<ApplyQualificationProject> {

    ApplyQualificationProject add(QualificationProjectAddRequest addRequest);

    Boolean batchAdd(List<QualificationProjectRequest> projectList, Long qualificationId);

    Boolean update(QualificationProjectUpdateRequest updateRequest);

    Boolean batchUpdate(List<QualificationProjectRequest> projectList, Long qualificationId);

    Boolean remove(Long projectId);

    void delByQualId(Long qualificationId);

    List<QualificationProjectResponse> listProjectInfoByQualId(Long qualificationId);

}

