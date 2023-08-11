package cn.qbs.wa.train.allowance.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.train.allowance.entity.ApplyAllowanceFee;
import cn.qbs.wa.train.allowance.mapper.ApplyAllowanceFeeMapper;
import cn.qbs.wa.train.allowance.pojo.applyallowancefee.*;
import cn.qbs.wa.train.allowance.service.ApplyAllowanceFeeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 资助资金申请明细-网络安全培训费用(ApplyAllowanceFee)表服务实现类
 *
 * @author makejava
 * @since 2022-11-03 19:43:28
 */
@Slf4j
@Service("applyAllowanceFeeService")
public class ApplyAllowanceFeeServiceImpl extends ServiceImpl<ApplyAllowanceFeeMapper, ApplyAllowanceFee> implements ApplyAllowanceFeeService {

    @Override
    public boolean add(ApplyAllowanceFeeAddRequest params) {
        ApplyAllowanceFee applyAllowanceFee = new ApplyAllowanceFee();
        BeanUtils.copyProperties(params, applyAllowanceFee);
        return this.save(applyAllowanceFee);
    }

    @Override
    public IPage<ApplyAllowanceFeePageResponse> page(ApplyAllowanceFeePageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public ApplyAllowanceFeeDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(ApplyAllowanceFeeUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        ApplyAllowanceFee applyAllowanceFee = new ApplyAllowanceFee();
        BeanUtils.copyProperties(params, applyAllowanceFee);
        return this.updateById(applyAllowanceFee);
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

