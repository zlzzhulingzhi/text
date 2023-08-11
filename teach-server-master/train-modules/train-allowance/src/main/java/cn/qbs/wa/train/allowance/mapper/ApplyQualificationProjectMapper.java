package cn.qbs.wa.train.allowance.mapper;

import cn.qbs.wa.train.allowance.entity.ApplyQualificationProject;
import cn.qbs.wa.train.allowance.pojo.apply.QualificationProjectResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 资助资格申请-项目计划(ApplyQualificationProject)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-11-02 11:20:28
 */
public interface ApplyQualificationProjectMapper extends BaseMapper<ApplyQualificationProject> {

    List<QualificationProjectResponse> listProjectInfoByQualId(Long qualificationId);
}

