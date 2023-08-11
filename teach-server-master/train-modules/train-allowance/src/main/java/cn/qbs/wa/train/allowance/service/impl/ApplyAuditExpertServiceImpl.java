package cn.qbs.wa.train.allowance.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.train.allowance.enums.ApplyTypeEnum;
import cn.qbs.wa.train.allowance.mapper.ApplyAuditExpertMapper;
import cn.qbs.wa.train.allowance.entity.ApplyAuditExpert;
import cn.qbs.wa.train.allowance.service.ApplyAuditExpertService;
import cn.qbs.wa.train.allowance.pojo.applyauditexpert.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 资助评审专家(ApplyAuditExpert)表服务实现类
 *
 * @author makejava
 * @since 2023-04-04 14:19:13
 */
@Slf4j
@Service("applyAuditExpertService")
public class ApplyAuditExpertServiceImpl extends ServiceImpl<ApplyAuditExpertMapper, ApplyAuditExpert> implements ApplyAuditExpertService {

    @Override
    public boolean add(ApplyAuditExpertAddRequest params) {
        ApplyAuditExpert applyAuditExpert = new ApplyAuditExpert();
        BeanUtils.copyProperties(params, applyAuditExpert);
        try {
            return this.save(applyAuditExpert);
        }catch (Exception e){
            throw new ServiceException("已有这位专家");
        }

    }

    @Override
    public List<ApplyAuditExpertDetailResponse> listApplyAuditExpert(ApplyAuditExpertListRequest request) {
        return baseMapper.listApplyAuditExpert(request);
    }

    @Override
    public boolean update(ApplyAuditExpertUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        ApplyAuditExpert applyAuditExpert = new ApplyAuditExpert();
        BeanUtils.copyProperties(params, applyAuditExpert);
        try {
            return this.updateById(applyAuditExpert);
        }catch (Exception e){
            throw new ServiceException("已有这位专家");
        }
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }
    
}

