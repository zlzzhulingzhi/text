package cn.qbs.wa.train.allowance.service;

import cn.qbs.wa.train.allowance.entity.ApplyQualification;
import cn.qbs.wa.train.allowance.pojo.apply.QualificationApplyRequest;
import cn.qbs.wa.train.allowance.pojo.apply.QualificationApplyResponse;
import cn.qbs.wa.train.allowance.pojo.apply.QualificationUpdateRequest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 资助资格申请(ApplyQualification)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2022-11-02 11:20:28
 */
public interface ApplyQualificationService extends IService<ApplyQualification> {

    /**
     * 资质申请
     * @param applyRequest 申请表单
     * @return 申请结果
     */
    ApplyQualification save(QualificationApplyRequest applyRequest);

    ApplyQualification copy(Long applyId);

    QualificationApplyResponse detail(Long applyId);

    Boolean update(QualificationUpdateRequest updateRequest);

    /**
     * 通过申请表单ID, 确认资质申请是否允许提交
     * @param applyId 申请表单ID
     */
    void checkCompleteness(Long applyId);

    void checkUniqueness(Long orgId);

    /**
     * 确认当前机构的资助申请是否通过
     *
     * @return true：通过；false：未通过/未申请
     */
    boolean checkPassStatus();
}

