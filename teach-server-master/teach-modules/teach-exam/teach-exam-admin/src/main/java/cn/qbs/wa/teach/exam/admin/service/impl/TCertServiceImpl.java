package cn.qbs.wa.teach.exam.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.cert.api.RemoteCertService;
import cn.qbs.wa.teach.cert.api.RemoteStandardTemplateService;
import cn.qbs.wa.teach.cert.api.pojo.DTO.cert.BatchGetCertRequestDTO;
import cn.qbs.wa.teach.cert.api.pojo.DTO.cert.CertDetailResponseDTO;
import cn.qbs.wa.teach.cert.api.pojo.DTO.cert.awardRecord.*;
import cn.qbs.wa.teach.cert.api.pojo.DTO.enums.CertSearchConfigEnum;
import cn.qbs.wa.teach.cert.api.pojo.DTO.template.StandardTemplateDetailResponseDTO;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.enums.EnabledEnum;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.exam.admin.mapper.TCertMapper;
import cn.qbs.wa.teach.exam.admin.pojo.exam.ExamRecord;
import cn.qbs.wa.teach.exam.common.entity.TCert;
import cn.qbs.wa.teach.exam.admin.service.TCertService;
import cn.qbs.wa.teach.exam.admin.pojo.tcert.*;
import cn.qbs.wa.teach.exam.common.entity.TCertRule;
import cn.qbs.wa.teach.exam.common.enumerate.ExamConditionEnum;
import cn.qbs.wa.teach.exam.common.enumerate.SourceTypeEnum;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spi.service.contexts.SecurityContext;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 任务证书表(TCert)表服务实现类
 *
 * @author makejava
 * @since 2022-05-16 13:47:59
 */
@Slf4j
@Service("tCertService")
public class TCertServiceImpl extends ServiceImpl<TCertMapper, TCert> implements TCertService {

    @Resource
    private TCertRuleServiceImpl tCertRuleService;

    @Resource
    private RemoteCertService remoteCertService;

    @Resource
    private RemoteStandardTemplateService remoteStandardTemplateService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean add(TCertAddRequest params) {
        List<TCertDetailResponse> certDetailResponseList = baseMapper.certDetail(params);
        if (CollUtil.isNotEmpty(certDetailResponseList)) {
            throw new IllegalParamsException("该证书和任务已关联");
        }
        //保存到任务证书规则表
        TCertRule tCertRule = new TCertRule();
        BeanUtils.copyProperties(params, tCertRule);
        tCertRule.setRuleCode(ExamConditionEnum.fromId(params.getRuleValue()).getCode());
        tCertRule.setRuleValue(params.getRuleValue().toString());
        tCertRuleService.save(tCertRule);

        TCert tCert = new TCert();
        BeanUtils.copyProperties(params, tCert);
        tCert.setDeleted(0);
        tCert.setId(tCertRule.getId());
        return this.save(tCert);
    }

    @Override
    public IPage<TCertPageResponse> page(TCertPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public TCertDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(TCertUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        TCert tCert = new TCert();
        BeanUtils.copyProperties(params, tCert);
        return this.updateById(tCert);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByIds(TCertUpdateRequest params) {
        return this.update(new UpdateWrapper<TCert>().eq("id", params.getId()).set("deleted", 1));
    }

    @Override
    public List<CertDetailResponse> certList(TCertDetailRequest request) {
        List<CertDetailResponse> certDetailResponseList = baseMapper.certList(request);
        if(CollUtil.isNotEmpty(certDetailResponseList)){
            certDetailResponseList.forEach(f ->{
                CertDetailResponseDTO data = remoteCertService.detail(new IdRequest(f.getCertId())).getData();
                Optional.ofNullable(data).ifPresent(p -> {
                    StandardTemplateDetailResponseDTO standardTemplateDetailResponseDTO = this.remoteStandardTemplateService.preview(new IdRequest(data.getTemplateId())).getData();
                    f.setCertImageUrl(standardTemplateDetailResponseDTO.getBackgroundUrl());
                    f.setName(p.getName());
                    f.setCertTemplateConfig(p.getCertTemplateConfig());
                    f.setCertSearchConfig(p.getCertSearchConfig());
                    f.setTemplateId(p.getTemplateId());
                    f.setText(ExamConditionEnum.fromName(f.getRuleCode()).getName());
                });
            });
        }
        return certDetailResponseList;
    }

    @Override
    public PageResultComDTO<AwardRecordPageResponseDTO> awardRecordPage(AwardRecordPageRequest awardRecordPageRequest) {
        CertDetailResponse certDetailResponse = baseMapper.awardRecordPage(awardRecordPageRequest);
        if(StringUtils.isNotNull(certDetailResponse)){
            AwardRecordPageRequestDTO recordPageRequest = new AwardRecordPageRequestDTO();
            recordPageRequest.setCertId(certDetailResponse.getCertId());
            if(StringUtils.isNoneBlank(awardRecordPageRequest.getUserName())){
                List<AwardRecordSearchListDTO> awardRecordSearchListDTOS = new ArrayList<>();
                AwardRecordSearchListDTO awardRecordSearchListDTO = new AwardRecordSearchListDTO();
                awardRecordSearchListDTO.setCode(CertSearchConfigEnum.USER_NAME.getCode());
                awardRecordSearchListDTO.setKeyValue(awardRecordPageRequest.getUserName());
                awardRecordSearchListDTOS.add(awardRecordSearchListDTO);
                recordPageRequest.setSearchList(awardRecordSearchListDTOS);
            }
            //recordPageRequest.setSourceId(certDetailResponse.getTaskId());
            return this.remoteCertService.awardRecordPage(recordPageRequest).getRemoteData();
        }
        return null;
    }

    @Override
    public List<PersonCertDetailResponseDTO> getCertInfo(ExamRecord examRecord) {
        TCertDetailRequest request = new TCertDetailRequest();
        request.setExamId(examRecord.getId());
        List<CertDetailResponse> certDetailResponses = baseMapper.certList(request);

        if(CollUtil.isNotEmpty(certDetailResponses)){
            List<Long> certIds = certDetailResponses.stream().map(CertDetailResponse::getCertId).collect(Collectors.toList());
            BatchGetCertRequestDTO batchGetCertRequestDTO = new BatchGetCertRequestDTO();
            batchGetCertRequestDTO.setCertIds(certIds);
            batchGetCertRequestDTO.setUserId(examRecord.getUserId());
            batchGetCertRequestDTO.setSourceId(examRecord.getId());
            batchGetCertRequestDTO.setEnabled(EnabledEnum.ENABLED.getValue());
            return remoteCertService.batchSelect(batchGetCertRequestDTO).getRemoteData();
        }
        return null;
    }
}

