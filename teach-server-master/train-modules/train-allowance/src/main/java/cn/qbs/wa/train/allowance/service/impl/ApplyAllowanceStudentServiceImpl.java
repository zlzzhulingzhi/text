package cn.qbs.wa.train.allowance.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.train.allowance.entity.ApplyAllowanceStudent;
import cn.qbs.wa.train.allowance.mapper.ApplyAllowanceStudentMapper;
import cn.qbs.wa.train.allowance.pojo.applyallowancestudent.*;
import cn.qbs.wa.train.allowance.service.ApplyAllowanceStudentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 资助资金申请明细-网络安全培训学员(ApplyAllowanceStudent)表服务实现类
 *
 * @author makejava
 * @since 2022-11-03 19:30:25
 */
@Slf4j
@Service("applyAllowanceStudentService")
public class ApplyAllowanceStudentServiceImpl extends ServiceImpl<ApplyAllowanceStudentMapper, ApplyAllowanceStudent> implements ApplyAllowanceStudentService {

    @Override
    public boolean add(ApplyAllowanceStudentAddRequest params) {
        ApplyAllowanceStudent applyAllowanceStudent = new ApplyAllowanceStudent();
        BeanUtils.copyProperties(params, applyAllowanceStudent);
        return this.save(applyAllowanceStudent);
    }

    @Override
    public IPage<ApplyAllowanceStudentPageResponse> page(ApplyAllowanceStudentPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public ApplyAllowanceStudentDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(ApplyAllowanceStudentUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        ApplyAllowanceStudent applyAllowanceStudent = new ApplyAllowanceStudent();
        BeanUtils.copyProperties(params, applyAllowanceStudent);
        return this.updateById(applyAllowanceStudent);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public void copyByApplyId(Long sourceAllowanceId, Long targetAllowanceId) {
        this.baseMapper.copyByApplyId(sourceAllowanceId, targetAllowanceId);
    }
}

