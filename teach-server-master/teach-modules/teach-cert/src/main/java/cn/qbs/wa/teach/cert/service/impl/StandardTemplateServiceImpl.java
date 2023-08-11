package cn.qbs.wa.teach.cert.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.cert.entity.Cert;
import cn.qbs.wa.teach.cert.entity.CertConfig;
import cn.qbs.wa.teach.cert.entity.StandardTemplate;
import cn.qbs.wa.teach.cert.entity.StandardTemplateConfig;
import cn.qbs.wa.teach.cert.enums.CertSearchConfigEnum;
import cn.qbs.wa.teach.cert.enums.CertTemplateConfigEnum;
import cn.qbs.wa.teach.cert.enums.TemplateConfigTypeEnum;
import cn.qbs.wa.teach.cert.mapper.CertConfigMapper;
import cn.qbs.wa.teach.cert.mapper.CertMapper;
import cn.qbs.wa.teach.cert.mapper.StandardTemplateMapper;
import cn.qbs.wa.teach.cert.pojo.common.CommonConfigDTO;
import cn.qbs.wa.teach.cert.pojo.common.CommonConfigDetailDTO;
import cn.qbs.wa.teach.cert.pojo.standardtemplate.*;
import cn.qbs.wa.teach.cert.service.CertConfigService;
import cn.qbs.wa.teach.cert.service.StandardTemplateConfigService;
import cn.qbs.wa.teach.cert.service.StandardTemplateService;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 证书模板(StandardTemplate)表服务实现类
 *
 * @author makejava
 * @since 2022-01-19 11:38:21
 */
@Slf4j
@Service("standardTemplateService")
public class StandardTemplateServiceImpl extends ServiceImpl<StandardTemplateMapper, StandardTemplate> implements StandardTemplateService {

    @Autowired
    StandardTemplateConfigService standardTemplateConfigService;

    @Autowired
    CertMapper certMapper;

    @Autowired
    CertConfigMapper certConfigMapper;

    @Resource
    private CertConfigService certConfigService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized boolean add(StandardTemplateAddRequest params) {
        List<StandardTemplate> templateList = list(new LambdaQueryWrapper<StandardTemplate>().eq(StandardTemplate::getName, params.getName()));
        if (CollUtil.isNotEmpty(templateList)) {
            throw new ServiceException("已存在相同名称的模板");
        }
        StandardTemplate standardTemplate = new StandardTemplate();
        BeanUtils.copyProperties(params, standardTemplate);
        boolean flag = this.save(standardTemplate);
        List<StandardTemplateConfig> standardTemplateConfigList = new ArrayList<>();
        setStandardTemplateConfigList(standardTemplateConfigList, params.getCertTemplateConfig().getConfigList(), TemplateConfigTypeEnum.CERT_CONFIG, standardTemplate.getId());
        //初始化查询配置
        List<CommonConfigDetailDTO> searchConfigList = new ArrayList<>();
        CertSearchConfigEnum[] values = CertSearchConfigEnum.values();
        for (CertSearchConfigEnum value : values) {
            CommonConfigDetailDTO commonConfigDetailDTO = new CommonConfigDetailDTO();
            BeanUtils.copyProperties(value, commonConfigDetailDTO);
            searchConfigList.add(commonConfigDetailDTO);
        }
        setStandardTemplateConfigList(standardTemplateConfigList, searchConfigList, TemplateConfigTypeEnum.SEARCH_CONFIG, standardTemplate.getId());
        standardTemplateConfigService.saveBatch(standardTemplateConfigList);
        return flag;
    }

    private void setStandardTemplateConfigList(List<StandardTemplateConfig> standardTemplateConfigList, List<CommonConfigDetailDTO> configList, TemplateConfigTypeEnum templateConfigTypeEnum, Long templateId) {
        if (CollUtil.isNotEmpty(configList)) {
            for (CommonConfigDetailDTO commonConfigDetailDTO : configList) {
                switch (templateConfigTypeEnum) {
                    case CERT_CONFIG:
                        if (commonConfigDetailDTO.getSelected()) {
                            StandardTemplateConfig standardTemplateConfig = new StandardTemplateConfig();
                            BeanUtils.copyProperties(commonConfigDetailDTO, standardTemplateConfig);
                            standardTemplateConfig.setType(templateConfigTypeEnum.getId());
                            standardTemplateConfig.setTemplateId(templateId);
                            standardTemplateConfigList.add(standardTemplateConfig);
                        }
                        break;
                    case SEARCH_CONFIG:
                        StandardTemplateConfig standardTemplateConfig = new StandardTemplateConfig();
                        BeanUtils.copyProperties(commonConfigDetailDTO, standardTemplateConfig);
                        standardTemplateConfig.setType(templateConfigTypeEnum.getId());
                        standardTemplateConfig.setTemplateId(templateId);
                        if(!commonConfigDetailDTO.getSelected()){
                            standardTemplateConfig.setEnabled(Constants.DB_FAIL);
                        } else {
                            standardTemplateConfig.setEnabled(Constants.DB_TRUE);
                        }
                        standardTemplateConfigList.add(standardTemplateConfig);
                        break;
                }

            }

        }
    }

    @Override
    public IPage<StandardTemplatePageResponse> page(StandardTemplatePageRequest params) {
        IPage<StandardTemplatePageResponse> page = baseMapper.page(params.createMpPage(), params);
        if (CollUtil.isNotEmpty(page.getRecords())) {
            List<StandardTemplatePageResponse> records = page.getRecords();
            Map<Long, List<StandardTemplateConfig>> templateGroup = getPageTemplateGroup(records.stream().map(StandardTemplatePageResponse::getId).collect(Collectors.toList()));
            setPageSearchConfig(records, templateGroup);
        }
        return page;
    }

    private void setPageSearchConfig(List<StandardTemplatePageResponse> records, Map<Long, List<StandardTemplateConfig>> templateGroup) {
        for (StandardTemplatePageResponse record : records) {
            if (templateGroup.containsKey(record.getId())) {
                CommonConfigDTO commonConfigDTO = new CommonConfigDTO();
                commonConfigDTO.setConfigList(BeanUtil.copyToList(templateGroup.get(record.getId()), CommonConfigDetailDTO.class));
                record.setCertSearchConfig(commonConfigDTO);
            }
        }
    }

    private Map<Long, List<StandardTemplateConfig>> getPageTemplateGroup(List<Long> templateIdList) {
        List<StandardTemplateConfig> templateConfigList = standardTemplateConfigService.list(new LambdaQueryWrapper<StandardTemplateConfig>().eq(StandardTemplateConfig::getType, TemplateConfigTypeEnum.SEARCH_CONFIG.getId())
                .in(StandardTemplateConfig::getTemplateId, templateIdList).eq(StandardTemplateConfig::getEnabled,Constants.DB_TRUE));
        return templateConfigList.stream().collect(Collectors.groupingBy(StandardTemplateConfig::getTemplateId));
    }

    @Override
    public StandardTemplateDetailResponse detail(Serializable id) {
        StandardTemplateDetailResponse standardTemplateDetailResponse = baseMapper.selectDetailById(id);
        if (standardTemplateDetailResponse != null) {
            CommonConfigDTO certTemplateConfig = new CommonConfigDTO();
            List<StandardTemplateConfig> templateConfigList = standardTemplateConfigService.list(new LambdaQueryWrapper<StandardTemplateConfig>().eq(StandardTemplateConfig::getTemplateId, id));
            setStandardTemplateConfigList(templateConfigList, TemplateConfigTypeEnum.CERT_CONFIG, certTemplateConfig);
            standardTemplateDetailResponse.setCertTemplateConfig(certTemplateConfig);
        }
        return standardTemplateDetailResponse;
    }

    private void setStandardTemplateConfigList(List<StandardTemplateConfig> templateConfigList, TemplateConfigTypeEnum templateConfigTypeEnum, CommonConfigDTO certConfig) {
        List<CommonConfigDetailDTO> commonConfigDetailDTOList = new ArrayList<>();
        if (CollUtil.isNotEmpty(templateConfigList)) {
            commonConfigDetailDTOList = templateConfigList.stream().filter(i -> templateConfigTypeEnum.getId().equals(i.getType())).map(i -> {
                CommonConfigDetailDTO commonConfigDetailDTO = new CommonConfigDetailDTO();
                BeanUtils.copyProperties(i, commonConfigDetailDTO);
                return commonConfigDetailDTO;
            }).collect(Collectors.toList());
        }
        switch (templateConfigTypeEnum) {
            case CERT_CONFIG:
                certConfig.setConfigList(getCertTemplateConfigDetail(commonConfigDetailDTOList));
                break;
            case SEARCH_CONFIG:
                certConfig.setConfigList(getCertSearchConfigDetail(commonConfigDetailDTOList));
                break;
            default:
                break;
        }

    }

    private List<CommonConfigDetailDTO> getCertTemplateConfigDetail(List<CommonConfigDetailDTO> commonConfigDetailDTOList) {
        Map<String, List<CommonConfigDetailDTO>> collectMap = commonConfigDetailDTOList.stream().collect(Collectors.groupingBy(CommonConfigDetailDTO::getCode));
        List<CommonConfigDetailDTO> commonConfigList = new ArrayList<>();
        CertTemplateConfigEnum[] values = CertTemplateConfigEnum.values();
        for (CertTemplateConfigEnum value : values) {
            CommonConfigDetailDTO commonConfigDetailDTO;
            if (collectMap.containsKey(value.getCode())) {
                commonConfigDetailDTO = collectMap.get(value.getCode()).get(0);
                commonConfigDetailDTO.setSelected(true);
                commonConfigDetailDTO.setExtraOperation(value.getExtraOperation());
                commonConfigDetailDTO.setMaxlength(value.getMaxlength());
                commonConfigDetailDTO.setPlaceholder(value.getPlaceholder());
                commonConfigDetailDTO.setEditable(value.getEditable());
            } else {
                commonConfigDetailDTO = new CommonConfigDetailDTO();
                BeanUtils.copyProperties(value, commonConfigDetailDTO);
                commonConfigDetailDTO.setSelected(false);
            }
            commonConfigList.add(commonConfigDetailDTO);
        }
        return commonConfigList;
    }

    private List<CommonConfigDetailDTO> getCertSearchConfigDetail(List<CommonConfigDetailDTO> commonConfigDetailDTOList) {
        Map<String, List<CommonConfigDetailDTO>> collectMap = commonConfigDetailDTOList.stream().collect(Collectors.groupingBy(CommonConfigDetailDTO::getCode));
        List<CommonConfigDetailDTO> commonConfigList = new ArrayList<>();
        CertSearchConfigEnum[] values = CertSearchConfigEnum.values();
        for (CertSearchConfigEnum value : values) {
            CommonConfigDetailDTO commonConfigDetailDTO;
            if (collectMap.containsKey(value.getCode())) {
                commonConfigDetailDTO = collectMap.get(value.getCode()).get(0);
                commonConfigDetailDTO.setSelected(true);
                commonConfigDetailDTO.setEditable(value.getEditable());
            } else {
                commonConfigDetailDTO = new CommonConfigDetailDTO();
                BeanUtils.copyProperties(value, commonConfigDetailDTO);
                commonConfigDetailDTO.setSelected(false);
            }
            commonConfigList.add(commonConfigDetailDTO);
        }
        return commonConfigList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(StandardTemplateUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        StandardTemplate standardTemplate = new StandardTemplate();
        BeanUtils.copyProperties(params, standardTemplate);
        boolean updateFlag = this.updateById(standardTemplate);
        standardTemplateConfigService.remove(new LambdaQueryWrapper<StandardTemplateConfig>().eq(StandardTemplateConfig::getTemplateId, params.getId()).eq(StandardTemplateConfig::getType, TemplateConfigTypeEnum.CERT_CONFIG.getId()));
        List<StandardTemplateConfig> standardTemplateConfigList = new ArrayList<>();
        setStandardTemplateConfigList(standardTemplateConfigList, params.getCertTemplateConfig().getConfigList(), TemplateConfigTypeEnum.CERT_CONFIG, params.getId());
        standardTemplateConfigService.saveBatch(standardTemplateConfigList);
        return updateFlag;
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        for (Long templateId : idList) {
            List<Cert> certList = certMapper.selectList(new LambdaQueryWrapper<Cert>().eq(Cert::getTemplateId, templateId));
            if (CollUtil.isNotEmpty(certList)) {
                throw new ServiceException("该模板已被引用,无法删除");
            }

        }
        return this.removeByIds(idList);
    }

    @Override
    public void batchEnabled(Integer flag, List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            throw new ServiceException("请选中");
        }
        List<StandardTemplate> templateList = new ArrayList<>();
        for (Long id : idList) {
            StandardTemplate standardTemplate = new StandardTemplate();
            standardTemplate.setId(id);
            standardTemplate.setEnabled(flag);
            templateList.add(standardTemplate);
        }
        updateBatchById(templateList);
    }

    @Override
    public Boolean getCheckCert(StandardTemplateCheckCertRequest request) {
        List<StandardTemplateConfig> list = standardTemplateConfigService.list(new LambdaQueryWrapper<StandardTemplateConfig>().eq(StandardTemplateConfig::getCode, CertTemplateConfigEnum.CERT_NUM_RULE.getCode()).eq(StandardTemplateConfig::getKeyValue, request.getCertNumRule()));
        return CollUtil.isEmpty(list);
    }

    @Override
    public StandardTemplateDetailSearchConfigResponse detailSearchConfig(Long id) {
        StandardTemplateDetailSearchConfigResponse standardTemplateDetailSearchConfigResponse = new StandardTemplateDetailSearchConfigResponse();
        CommonConfigDTO certSearchConfig = new CommonConfigDTO();
        List<StandardTemplateConfig> templateConfigList = standardTemplateConfigService.list(new LambdaQueryWrapper<StandardTemplateConfig>().eq(StandardTemplateConfig::getTemplateId, id).eq(StandardTemplateConfig::getEnabled, Constants.DB_TRUE));
        setStandardTemplateConfigList(templateConfigList, TemplateConfigTypeEnum.SEARCH_CONFIG, certSearchConfig);
        standardTemplateDetailSearchConfigResponse.setCertSearchConfig(certSearchConfig);
        return standardTemplateDetailSearchConfigResponse;
    }

    @Override
    @Transactional
    public void updateSearchConfig(StandardTemplateUpdateSearchConfigRequest params) {
        List<StandardTemplateConfig> standardTemplateConfigList = new ArrayList<>();
        standardTemplateConfigService.remove(new LambdaQueryWrapper<StandardTemplateConfig>().eq(StandardTemplateConfig::getTemplateId, params.getId()).eq(StandardTemplateConfig::getType, TemplateConfigTypeEnum.SEARCH_CONFIG.getId()));
        setStandardTemplateConfigList(standardTemplateConfigList, params.getCertSearchConfig().getConfigList(), TemplateConfigTypeEnum.SEARCH_CONFIG, params.getId());
        standardTemplateConfigService.saveBatch(standardTemplateConfigList);
        List<Cert> certList = certMapper.selectList(new LambdaQueryWrapper<Cert>().eq(Cert::getTemplateId, params.getId()));
        updateCertConfig(certList, standardTemplateConfigList);
    }

    private void updateCertConfig(List<Cert> certList, List<StandardTemplateConfig> standardTemplateConfigList){
        Map<String, Integer> TemplateConfigMap = standardTemplateConfigList.stream().collect(Collectors.toMap(StandardTemplateConfig::getCode, StandardTemplateConfig::getEnabled));
        for (Cert cert : certList){
            List<CertConfig> certConfig = certConfigMapper.selectList(new LambdaQueryWrapper<CertConfig>().eq(CertConfig::getCertId, cert.getId()));
            certConfig.forEach(i -> i.setEnabled(TemplateConfigMap.get(i.getCode())));
            certConfigService.updateBatchById(certConfig);
        }
    }
    @Override
    public StandardTemplateDetailResponse preview(Long id) {
        StandardTemplateDetailResponse detail = detail(id);
        CommonConfigDTO certTemplateConfig = detail.getCertTemplateConfig();
        certTemplateConfig.setConfigList(certTemplateConfig.getConfigList().stream().filter(i->i.getSelected()).collect(Collectors.toList()));
        return detail;
    }


}

