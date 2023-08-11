package cn.qbs.wa.train.allowance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.train.allowance.entity.ApplyAttach;
import cn.qbs.wa.train.allowance.enums.ApplyTypeEnum;
import cn.qbs.wa.train.allowance.enums.AttachSectionEnum;
import cn.qbs.wa.train.allowance.mapper.ApplyAttachMapper;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyAttachAddRequest;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyAttachResponse;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyExpertAttachAddRequest;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyExpertAttachListRequest;
import cn.qbs.wa.train.allowance.service.ApplyAttachService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 资助评审附件(ApplyAttach)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2022-11-02 18:59:29
 */
@Slf4j
@Service("applyAttachService")
public class ApplyAttachServiceImpl extends ServiceImpl<ApplyAttachMapper, ApplyAttach> implements ApplyAttachService {

    @Override
    public List<ApplyAttachResponse> saveBatch(ApplyTypeEnum applyTypeEnum, AttachSectionEnum sectionEnum, ApplyAttachAddRequest attachRequest) {
        if (CollUtil.isNotEmpty(attachRequest.getAttachList())) {
            String applyType = applyTypeEnum.getCode();
            String section = sectionEnum.getCode();
            Long applyId = attachRequest.getApplyId();
            List<ApplyAttach> attachList = CollStreamUtil.toList(attachRequest.getAttachList(), attach -> {
                ApplyAttach applyAttach = BeanUtil.copyProperties(attach, ApplyAttach.class);
                applyAttach.setApplyId(applyId);
                applyAttach.setApplyType(applyType);
                applyAttach.setSection(section);
                return applyAttach;
            });
            this.saveBatch(attachList);
            return BeanUtil.copyToList(attachList, ApplyAttachResponse.class);
        }
        return Collections.emptyList();
    }

    @Override
    public List<ApplyAttachResponse> saveBatchExpertAttach(AttachSectionEnum sectionEnum, ApplyExpertAttachAddRequest attachRequest) {
        if (CollUtil.isNotEmpty(attachRequest.getAttachList())) {
            String applyType = attachRequest.getApplyType();
            String section = sectionEnum.getCode();
            Long applyId = attachRequest.getApplyId();
            List<ApplyAttach> attachList = CollStreamUtil.toList(attachRequest.getAttachList(), attach -> {
                ApplyAttach applyAttach = BeanUtil.copyProperties(attach, ApplyAttach.class);
                applyAttach.setApplyId(applyId);
                applyAttach.setApplyType(applyType);
                applyAttach.setSection(section);
                return applyAttach;
            });
            this.saveBatch(attachList);
            return BeanUtil.copyToList(attachList, ApplyAttachResponse.class);
        }
        return Collections.emptyList();
    }

    @Override
    public List<ApplyAttachResponse> list(ApplyTypeEnum applyTypeEnum, AttachSectionEnum sectionEnum, Long applyId) {
        String applyType = applyTypeEnum.getCode();
        String section = sectionEnum.getCode();
        List<ApplyAttach> list = this.lambdaQuery().eq(ApplyAttach::getApplyId, applyId).eq(ApplyAttach::getApplyType, applyType).eq(ApplyAttach::getSection, section).list();
        return BeanUtil.copyToList(list, ApplyAttachResponse.class);
    }

    @Override
    public List<ApplyAttachResponse> listExpertAttach(AttachSectionEnum sectionEnum, ApplyExpertAttachListRequest request) {
        String applyType = request.getApplyType();
        String section = sectionEnum.getCode();
        List<ApplyAttach> list = this.lambdaQuery().eq(ApplyAttach::getApplyId, request.getId()).eq(ApplyAttach::getApplyType, applyType).eq(ApplyAttach::getSection, section).list();
        return BeanUtil.copyToList(list, ApplyAttachResponse.class);
    }
}

