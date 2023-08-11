package cn.qbs.wa.teach.cert.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.basic.api.RemoteUserService;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserListDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserListFieldDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserListResultDTO;
import cn.qbs.wa.teach.cert.entity.*;
import cn.qbs.wa.teach.cert.enums.AwardBusinessTypeEnum;
import cn.qbs.wa.teach.cert.enums.CertSearchConfigEnum;
import cn.qbs.wa.teach.cert.enums.CertTemplateConfigEnum;
import cn.qbs.wa.teach.cert.enums.TemplateConfigTypeEnum;
import cn.qbs.wa.teach.cert.handler.AwardHandler;
import cn.qbs.wa.teach.cert.handler.AwardPostHandler;
import cn.qbs.wa.teach.cert.mapper.CertMapper;
import cn.qbs.wa.teach.cert.mapper.StandardTemplateConfigMapper;
import cn.qbs.wa.teach.cert.mapper.StandardTemplateMapper;
import cn.qbs.wa.teach.cert.pojo.awardrecord.*;
import cn.qbs.wa.teach.cert.pojo.cert.*;
import cn.qbs.wa.teach.cert.pojo.common.CommonConfigDTO;
import cn.qbs.wa.teach.cert.pojo.common.CommonConfigDetailDTO;
import cn.qbs.wa.teach.cert.pojo.common.CommonConfigDetailLiteDTO;
import cn.qbs.wa.teach.cert.service.*;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.BatchFlagRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import cn.qbs.wa.teach.common.post.drawable.Image;
import cn.qbs.wa.teach.common.post.drawable.Poster;
import cn.qbs.wa.teach.common.post.drawable.Text;
import cn.qbs.wa.teach.exam.api.RemoteExamApiService;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamineeQueryDTO;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamineeeDTO;
import cn.qbs.wa.teach.notification.api.RemoteNotificationService;
import cn.qbs.wa.teach.notification.api.enums.NotificationBusinessEnum;
import cn.qbs.wa.teach.notification.api.pojo.DTO.notification.SendNotificationDTO;
import cn.qbs.wa.teach.notification.api.pojo.DTO.notification.SendNotificationUserInfoDTO;
import cn.qbs.wa.teach.organization.api.RemoteEmployeeDeptService;
import cn.qbs.wa.teach.organization.api.RemoteEmployeeService;
import cn.qbs.wa.teach.organization.api.RemoteStudentService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.dept.EmployeeDeptListSearchDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.dept.EmployeeDeptTreeListDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.employee.EmployeePageResultDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.employee.EmployeePageSearchDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentSearchDTO;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 证书(Cert)表服务实现类
 *
 * @author makejava
 * @since 2022-01-19 19:19:21
 */
@Slf4j
@Service("certService")
public class CertServiceImpl extends ServiceImpl<CertMapper, Cert> implements CertService {


    @Autowired
    RemoteEmployeeDeptService remoteEmployeeDeptService;

    @Autowired
    RemoteExamApiService remoteExamApiService;

    @Autowired
    RemoteUserService remoteUserService;

    @Autowired
    RemoteEmployeeService remoteEmployeeService;

    @Autowired
    CertConfigService certConfigService;

    @Autowired
    AwardRecordBusinessService awardRecordBusinessService;

    @Autowired
    StandardTemplateMapper standardTemplateMapper;

    @Autowired
    StandardTemplateConfigMapper standardTemplateConfigMapper;

    @Autowired
    AwardRecordService awardRecordService;

    @Autowired
    AwardRecordDetailService awardRecordDetailService;

    @Autowired
    RemoteStudentService remoteStudentService;

    @Autowired
    AwardHandler awardHandler;

    @Autowired
    AwardPostHandler awardPostHandler;

    @Autowired
    CertPostService certPostService;

    @Autowired
    RemoteNotificationService remoteNotificationService;

    @Autowired
    CertMapper certMapper;

    @Value("${open.url}")
    private String openUrl;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Cert add(CertAddRequest params) {
        CertListRequest certDetailRequest = new CertListRequest();
        BeanUtils.copyProperties(params, certDetailRequest);
        List<CertDetailResponse> data = repetition(certDetailRequest);
        if (CollUtil.isNotEmpty(data)) {
            throw new ServiceException("证书名重复！");
        }
        log.info("证书新增传参{}", JSON.toJSONString(params));
        StandardTemplate standardTemplate = standardTemplateMapper.selectById(params.getTemplateId());
        Cert cert = new Cert();
        BeanUtils.copyProperties(params, cert);
        cert.setTemplateName(standardTemplate.getName());
        cert.setModelType(standardTemplate.getModelType());
        boolean saveFlag = this.save(cert);

        //实例化配置
        List<StandardTemplateConfig> standardTemplateConfigList = standardTemplateConfigMapper.selectList(new LambdaQueryWrapper<StandardTemplateConfig>().eq(StandardTemplateConfig::getTemplateId, params.getTemplateId()));
        List<CertConfig> certConfigList = standardTemplateConfigList.stream().map(i -> getCertConfig(i, cert.getId())).collect(Collectors.toList());
        certConfigService.saveBatch(certConfigList);

        return cert;
    }

    private List<AwardRecord> getUpdateAwardRecordCertList(List<AwardRecord> awardRecordList) {
        List<AwardRecord> awardRecordCertList = new ArrayList<>();
        for (AwardRecord awardRecord : awardRecordList) {
            AwardRecord awardCertRecord = new AwardRecord();
            awardCertRecord.setId(awardRecord.getId());
            awardCertRecord.setCertPrefix(awardRecord.getCertPrefix());
            awardCertRecord.setCertNum(awardRecord.getCertNum());
            awardRecordCertList.add(awardCertRecord);
        }
        return awardRecordCertList;
    }

    private List<AwardRecordBusiness> getAwardRecordBusinessList(List<CertUserResponse> totalUserList, List<AwardRecord> awardRecordList) {
        List<AwardRecordBusiness> awardRecordBusinessList = new ArrayList<>();
        Map<Long, Long> awardUserMap = awardRecordList.stream().collect(Collectors.toMap(AwardRecord::getUserId, AwardRecord::getId));
        for (CertUserResponse certUserResponse : totalUserList) {
            if (certUserResponse.getExamId() != null) {
                AwardRecordBusiness awardRecordBusiness = new AwardRecordBusiness();
                awardRecordBusiness.setAwardRecordId(awardUserMap.get(certUserResponse.getUserId()));
                awardRecordBusiness.setBusinessId(certUserResponse.getExamId());
                awardRecordBusiness.setBusinessType(AwardBusinessTypeEnum.EXAM.getId());
                awardRecordBusiness.setBusinessName(certUserResponse.getExamName());
                awardRecordBusiness.setBusinessContent(certUserResponse.getScore().toString());
                awardRecordBusinessList.add(awardRecordBusiness);
            }
        }
        return awardRecordBusinessList;
    }

    private List<AwardRecord> getAwardRecordList(List<CertUserResponse> totalUserList, CertAwardRequest params) {
        List<AwardRecord> awardRecordList = new ArrayList<>();
        for (CertUserResponse certUserResponse : totalUserList) {
            AwardRecord awardRecord = new AwardRecord();
            BeanUtil.copyProperties(certUserResponse, awardRecord);
            awardRecord.setId(null);
            awardRecord.setCertId(params.getCertId());
            awardRecord.setSourceMark(params.getSourceMark());
            awardRecord.setSourceId(params.getSourceId());
            awardRecord.setSourceType(params.getSourceType());
            awardRecord.setEnabled(Constants.DB_TRUE);
            awardRecordList.add(awardRecord);
        }
        return awardRecordList;
    }


    private CertConfig getCertConfig(StandardTemplateConfig standardTemplateConfig, Long certId) {
        CertConfig certConfig = new CertConfig();
        BeanUtil.copyProperties(standardTemplateConfig, certConfig);
        certConfig.setCertId(certId);
        certConfig.setId(null);
        return certConfig;
    }


    private List<CertUserResponse> getTotalUserListDetail(List<CertUserResponse> examUserList, List<CertUserResponse> userList) {
        List<CertUserResponse> totalUserList = new ArrayList<>();
        if (CollUtil.isNotEmpty(examUserList)) {
            totalUserList.addAll(examUserList);
        }
        if (CollUtil.isNotEmpty(userList)) {
            totalUserList.addAll(userList);
        }

        if (CollUtil.isEmpty(totalUserList)) {
            throw new ServiceException("没有添加用户");
        }

        return totalUserList.stream().filter(TreeUtil.distinctByKey(CertUserResponse::getUserId)).collect(Collectors.toList());
    }

    @Override
    public IPage<CertPageResponse> page(CertPageRequest params) {
        IPage<CertPageResponse> page = baseMapper.page(params.createMpPage(), params);
        if (CollUtil.isNotEmpty(page.getRecords())) {
            setAwardCount(page);
        }
        return page;
    }

    private void setAwardCount(IPage<CertPageResponse> page) {
        List<Long> certIdList = page.getRecords().stream().map(CertPageResponse::getId).collect(Collectors.toList());
        List<AwardRecordCountResponse> awardCountList = awardRecordService.listAwardCount(certIdList);
        if (CollUtil.isNotEmpty(awardCountList)) {
            Map<Long, List<AwardRecordCountResponse>> certGroup = awardCountList.stream().collect(Collectors.groupingBy(AwardRecordCountResponse::getCertId));
            List<CertPageResponse> records = page.getRecords();
            for (CertPageResponse record : records) {
                if (certGroup.containsKey(record.getId())) {
                    AwardRecordCountResponse awardRecordCountResponse = certGroup.get(record.getId()).get(0);
                    record.setAwardCount(awardRecordCountResponse.getAwardCount());
                    record.setAwardEffectiveCount(awardRecordCountResponse.getAwardEffectiveCount());
                    record.setAwardInvalidCount(awardRecordCountResponse.getAwardCount() - awardRecordCountResponse.getAwardEffectiveCount());
                }
            }

        }
    }

    @Override
    public CertDetailResponse detail(Serializable id) {
        CertDetailResponse certDetailResponse = new CertDetailResponse();
        Cert cert = getById(id);
        BeanUtil.copyProperties(cert, certDetailResponse);

        //注入筛选配置
        CommonConfigDTO certSearchConfig = new CommonConfigDTO();
        CommonConfigDTO certTemplateConfig = new CommonConfigDTO();
        List<CertConfig> certConfigList = certConfigService.list(new LambdaQueryWrapper<CertConfig>().eq(CertConfig::getCertId, cert.getId()).eq(CertConfig::getEnabled, Constants.DB_TRUE));
        List<StandardTemplateConfig> templateConfigList = BeanUtil.copyToList(certConfigList, StandardTemplateConfig.class);
        //  setStandardTemplateConfigList(templateConfigList, TemplateConfigTypeEnum.SEARCH_CONFIG, certSearchConfig);
        setStandardTemplateConfigList(templateConfigList, TemplateConfigTypeEnum.CERT_CONFIG, certTemplateConfig);
        certDetailResponse.setCertTemplateConfig(certTemplateConfig);
        certDetailResponse.setCertSearchConfig(certSearchConfig);

        return certDetailResponse;
    }

    private void setStandardTemplateConfigList(List<StandardTemplateConfig> templateConfigList, TemplateConfigTypeEnum templateConfigTypeEnum, CommonConfigDTO certConfig) {
        if (CollUtil.isNotEmpty(templateConfigList)) {
            List<CommonConfigDetailDTO> commonConfigDetailDTOList = templateConfigList.stream().filter(i -> templateConfigTypeEnum.getId().equals(i.getType())).map(i -> {
                CommonConfigDetailDTO commonConfigDetailDTO = new CommonConfigDetailDTO();
                BeanUtils.copyProperties(i, commonConfigDetailDTO);
                return commonConfigDetailDTO;
            }).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(commonConfigDetailDTOList)) {
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
                commonConfigList.add(commonConfigDetailDTO);
            }
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
            } else {
                commonConfigDetailDTO = new CommonConfigDetailDTO();
                BeanUtils.copyProperties(value, commonConfigDetailDTO);
            }
            commonConfigList.add(commonConfigDetailDTO);
        }
        return commonConfigList;
    }

    private CertExamResponse getExamInfo(List<CertUserResponse> userList) {
        List<CertUserResponse> examUserList = userList.stream().filter(i -> i.getScore() != null).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(examUserList)) {
            CertUserResponse certUserResponse = examUserList.get(0);
            CertExamResponse certExamResponse = new CertExamResponse();
            certExamResponse.setExamId(certUserResponse.getExamId());
            certExamResponse.setExamName(certUserResponse.getExamName());
            certExamResponse.setExamUserList(examUserList);
            return certExamResponse;
        }
        return null;
    }

    @Override
    public boolean update(CertUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        Cert cert = new Cert();
        BeanUtils.copyProperties(params, cert);
        return this.updateById(cert);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        if (CollUtil.isNotEmpty(idList)) {
            for (Long certId : idList) {
                List<AwardRecord> awardRecordList = awardRecordService.list(new LambdaQueryWrapper<AwardRecord>().eq(AwardRecord::getCertId, certId));
                if (CollUtil.isNotEmpty(awardRecordList)) {
                    throw new ServiceException("该证书已被颁发,无法删除");
                }
            }
        }
        return this.removeByIds(idList);
    }

    @Override
    public void batchEnabled(Integer flag, List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            throw new ServiceException("请选中");
        }
        List<Cert> certList = new ArrayList<>();
        for (Long id : idList) {
            Cert cert = new Cert();
            cert.setId(id);
            cert.setEnabled(flag);
            certList.add(cert);
        }
        updateBatchById(certList);
    }

    @Override
    public List<CertUserTreeResponse> getUserList(CertUserListRequest params) {
        EmployeeDeptListSearchDTO employeeDeptListSearchDTO = new EmployeeDeptListSearchDTO();
        R<List<EmployeeDeptTreeListDTO>> treeListResult = remoteEmployeeDeptService.listTree(employeeDeptListSearchDTO);
        if (R.SUCCESS == treeListResult.getCode()) {
            List<CertUserTreeResponse> certUserResponseList = new ArrayList<>();
            List<EmployeeDeptTreeListDTO> data = treeListResult.getData();
            if (CollUtil.isNotEmpty(data)) {
                for (EmployeeDeptTreeListDTO employeeDeptTreeListDTO : data) {
                    CertUserTreeResponse certUserTreeResponse = new CertUserTreeResponse();
                    BeanUtil.copyProperties(employeeDeptTreeListDTO, certUserTreeResponse);
                    certUserResponseList.add(certUserTreeResponse);
                }
                return certUserResponseList;
            }
        }
        return null;
    }

    @Override
    public List<CertUserResponse> getExamUserList(CertExamUserListRequest params) {
        ExamineeQueryDTO examineeQueryDTO = new ExamineeQueryDTO();
        BeanUtil.copyProperties(params, examineeQueryDTO);
        R<List<ExamineeeDTO>> listR = remoteExamApiService.examineeList(examineeQueryDTO);
        if (CollUtil.isNotEmpty(listR.getRemoteData())) {
            List<CertUserResponse> certUserResponses = BeanUtil.copyToList(listR.getData(), CertUserResponse.class);
            setPhoneAndIdNumInfoByCertUserResponse(certUserResponses);
            setAwardedResponse(certUserResponses, params.getCertId());
            return certUserResponses;
        }
        return null;
    }

    @Override
    public IPage<CertPageResponse> myPage(CertPageRequest params) {
        return baseMapper.myPage(params.createMpPage(), params);
    }

    @Override
    public List<CertListResponse> mySelectList(CertListRequest params) {
        return baseMapper.mySelectList(params);
    }

    @Override
    public MyCertSearchResponse myCertSearch(MyCertSearchRequest params) {
        List<AwardRecord> awardRecordList = filterAwardRecords(params.getSearchConfigList(), params.getId());
        if (CollUtil.isNotEmpty(awardRecordList)) {
            awardRecordList = awardRecordList.stream().filter(i -> Constants.DB_TRUE.equals(i.getEnabled())).collect(Collectors.toList());
            if (CollUtil.isEmpty(awardRecordList)) {
                throw new ServiceException("该证书状态异常,请联系管理员");
            }
            MyCertSearchResponse myCertSearchResponse = new MyCertSearchResponse();
            BeanUtil.copyProperties(awardRecordList.get(0), myCertSearchResponse);
            return myCertSearchResponse;
        }
        return null;
    }

    private List<AwardRecord> filterAwardRecords(List<CommonConfigDetailLiteDTO> userInfoSearchList, Long certId) {
        List<AwardRecord> filterAwardRecords = new ArrayList<>();
        AwardRecordListRequest awardRecordListRequest = new AwardRecordListRequest();
        awardRecordListRequest.setSearchList(BeanUtil.copyToList(userInfoSearchList, AwardRecordSearchList.class));
        awardRecordListRequest.setCertId(certId);
        List<AwardRecord> awardRecords = awardRecordService.selectConditionList(awardRecordListRequest);
        if (CollUtil.isNotEmpty(awardRecords)) {
            Map<Long, List<AwardRecord>> awardRecordGroup = awardRecords.stream().collect(Collectors.groupingBy(AwardRecord::getId));
            for (Long recordId : awardRecordGroup.keySet()) {
                List<AwardRecord> awardRecordSearchList = awardRecordGroup.get(recordId);
                if (CollUtil.isNotEmpty(awardRecordSearchList) && userInfoSearchList.size() == awardRecordSearchList.size()) {
                    filterAwardRecords.add(awardRecordSearchList.get(0));
                }
            }
        }
        return filterAwardRecords;
    }


    private List<Long> getUserIdList(List<CommonConfigDetailLiteDTO> userInfoSearchList, Long certId) {
        AwardRecordListRequest awardRecordListRequest = new AwardRecordListRequest();
        awardRecordListRequest.setSearchList(BeanUtil.copyToList(userInfoSearchList, AwardRecordSearchList.class));
        awardRecordListRequest.setCertId(certId);
        List<AwardRecord> awardRecords = awardRecordService.selectConditionList(awardRecordListRequest);
        if (CollUtil.isNotEmpty(awardRecords)) {
            Map<Long, List<AwardRecord>> userCertTempMap = awardRecords.stream().collect(Collectors.groupingBy(AwardRecord::getUserId));
            List<Long> userIdList = new ArrayList<>();
            for (Long userId : userCertTempMap.keySet()) {
                List<AwardRecord> awardRecordList = userCertTempMap.get(userId);
                if (awardRecordList.size() == userInfoSearchList.size()) {
                    userIdList.add(userId);
                }
            }
            return userIdList;
        }
        return null;
    }

    @Override
    @Async
    public void certPost(CertAwardRequest certAwardRequest) {
        SecurityContextHolder.setSelectOrgId(certAwardRequest.getOrgId().toString());
        List<AwardRecord> awardRecordList = awardRecordService.list(new LambdaQueryWrapper<AwardRecord>().eq(AwardRecord::getCertId, certAwardRequest.getCertId()).isNull(AwardRecord::getCertImageUrl));
        certPostAwardRecordList(awardRecordList);
    }

    private void certPostAwardRecordList(List<AwardRecord> awardRecordList) {
        if (CollUtil.isNotEmpty(awardRecordList)) {
            List<Long> awardRecordIdList = awardRecordList.stream().map(AwardRecord::getId).collect(Collectors.toList());
            Map<Long, List<AwardRecordDetail>> awardRecordDetailGroup = awardRecordDetailService.list(new LambdaQueryWrapper<AwardRecordDetail>().eq(AwardRecordDetail::getType, TemplateConfigTypeEnum.CERT_CONFIG.getId()).in(AwardRecordDetail::getAwardRecordId, awardRecordIdList)).stream().collect(Collectors.groupingBy(AwardRecordDetail::getAwardRecordId));
            Map<Long, String> awardPostUrlMap = certPostService.addCertPost(getPostList(awardRecordDetailGroup));
            List<AwardRecord> awardRecordUpdateList = getAwardRecordUpdateList(awardPostUrlMap);
            awardRecordService.updateBatchById(awardRecordUpdateList);
        }
    }

    @Override
    public void batchEnable(BatchFlagRequest batchFlagRequest) {
        List<Long> idList = batchFlagRequest.getIdList();
        List<AwardRecord> awardRecords = new ArrayList<>();
        for (Long awardRecordId : idList) {
            AwardRecord awardRecord = new AwardRecord();
            awardRecord.setId(awardRecordId);
            awardRecord.setEnabled(batchFlagRequest.getFlag());
            awardRecords.add(awardRecord);
        }
        awardRecordService.updateBatchById(awardRecords);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void award(CertAwardRequest params) {
        log.info("证书新增传参{}", JSON.toJSONString(params));
        //目前可以重复颁发
        List<CertUserResponse> totalUserList = getTotalUserList(params.getUserList(), params.getCertId());
        //List<CertUserResponse> totalUserList = params.getUserList();
        List<CertConfig> certConfigList = certConfigService.list(new LambdaQueryWrapper<CertConfig>().eq(CertConfig::getCertId, params.getCertId()).orderByAsc(CertConfig::getType));

        List<StandardTemplateConfig> standardTemplateConfigList = BeanUtil.copyToList(certConfigList, StandardTemplateConfig.class);

        //开始保存颁发记录(这里还缺失证书编号跟规则)
        List<AwardRecord> awardRecordList = getAwardRecordList(totalUserList, params);


        awardRecordService.saveBatch(awardRecordList);

        notification(awardRecordList, params.getCertId());


        //有考试记录的要另存关系表
        List<AwardRecordBusiness> awardRecordBusinessList = getAwardRecordBusinessList(totalUserList, awardRecordList);
        if (CollUtil.isNotEmpty(awardRecordBusinessList)) {
            awardRecordBusinessService.saveBatch(awardRecordBusinessList);
        }


        //存颁发生明细数据

        List<AwardRecordDetail> awardRecordDetailList = awardHandler.load(awardRecordList, standardTemplateConfigList, awardRecordBusinessList);
        awardRecordDetailService.saveBatch(awardRecordDetailList);

        //这里已经在load补充了证书编号和规则,重新更新1次
        awardRecordService.updateBatchById(getUpdateAwardRecordCertList(awardRecordList));

        //补充orgId方便后边证书生成
        if (params.getOrgId() == null) {
            params.setOrgId(SecurityContextHolder.getOrgId());
        }


    }

    private void notification(List<AwardRecord> awardRecordList, Long certId) {
        Cert cert = certMapper.selectById(certId);
        for (AwardRecord awardRecord : awardRecordList) {
            SendNotificationDTO sendCommonNotificationDTO = new SendNotificationDTO();

            List<SendNotificationUserInfoDTO> userList = new ArrayList<>();
            SendNotificationUserInfoDTO sendNotificationUserInfoDTO = new SendNotificationUserInfoDTO();
            sendNotificationUserInfoDTO.setUserId(awardRecord.getUserId());
            userList.add(sendNotificationUserInfoDTO);
            sendCommonNotificationDTO.setUserInfo(userList);

            sendCommonNotificationDTO.setOrgId(cert.getOrgId());
            sendCommonNotificationDTO.setBusinessType(NotificationBusinessEnum.GOT_CREDENTIAL);
            sendCommonNotificationDTO.setBusinessId(awardRecord.getId());

            Map<String, String> valueMap = new HashMap<>();
            valueMap.put("credentialName", cert.getName());
            String template = "{}/Business/Cert/";
            valueMap.put("url", StrUtil.format(template, openUrl));
            sendCommonNotificationDTO.setTemplateKey(valueMap);

            log.info("站内信通知{}", JSON.toJSONString(sendCommonNotificationDTO));
            remoteNotificationService.sendCommonNotification(sendCommonNotificationDTO, SecurityConstants.INNER);

        }
    }

    private List<CertUserResponse> getTotalUserList(List<CertUserResponse> userList, Long certId) {
        List<AwardRecord> existList = awardRecordService.list(new LambdaQueryWrapper<AwardRecord>().select(AwardRecord::getUserId).eq(AwardRecord::getCertId, certId).eq(AwardRecord::getEnabled, Constants.DB_TRUE));
        if (CollUtil.isNotEmpty(existList)) {
            List<Long> userIdList = existList.stream().map(AwardRecord::getUserId).collect(Collectors.toList());
            return userList.stream().filter(i -> !userIdList.contains(i.getUserId())).collect(Collectors.toList());
        }
        return userList;
    }

    @Override
    public IPage<AwardRecordPageResponse> awardRecordPage(AwardRecordPageRequest recordPageRequest) {
        IPage<AwardRecordPageResponse> page;
        recordPageRequest.setSearchList(filterSearchList(recordPageRequest.getSearchList()));
        if (CollUtil.isNotEmpty(recordPageRequest.getSearchList())) {
            page = awardRecordService.selectConditionPage(recordPageRequest);
        } else {
            page = awardRecordService.page(recordPageRequest);
        }
        setAwardRecordUserInfo(page);
        //这里有可能实例化没有身份证号码或者手机号码这时需要重新查找
        setPhoneAndIdNumInfoByAwardRecordResponse(page.getRecords());
        return page;
    }

    private void setPhoneAndIdNumInfoByAwardRecordResponse(List<AwardRecordPageResponse> records) {
        if (CollUtil.isNotEmpty(records)) {
            List<Long> userIdList = records.stream().map(AwardRecordPageResponse::getUserId).collect(Collectors.toList());
            StudentSearchDTO studentSearchDTO = new StudentSearchDTO();
            studentSearchDTO.setUserIds(userIdList);
            if (CollUtil.isNotEmpty(remoteStudentService.list(studentSearchDTO).getRemoteData())) {
                Map<Long, StudentDTO> userMap = remoteStudentService.list(studentSearchDTO).getRemoteData().stream().collect(Collectors.toMap(StudentDTO::getUserId, i -> i));
                for (AwardRecordPageResponse record : records) {
                    if (userMap.containsKey(record.getUserId())) {
                        if (StrUtil.isBlank(record.getIdNum())) {
                            record.setIdNum(userMap.get(record.getUserId()).getIdNumber());
                        }
                        if (StrUtil.isBlank(record.getPhone())) {
                            record.setPhone(userMap.get(record.getUserId()).getAccount());
                        }
                    }
                }
            }

        }
    }

    private List<AwardRecordSearchList> filterSearchList(List<AwardRecordSearchList> searchList) {
        if (CollUtil.isNotEmpty(searchList)) {
            return searchList.stream().filter(i -> StrUtil.isNotBlank(i.getKeyValue())).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public IPage<CertUserResponse> getUserPage(CertUserPageRequest params) {
        //身份证号码要去basic服务查询 其他直接通过org服务查询即可

        EmployeePageSearchDTO employeePageSearchDTO = setEmployeeInfoPageSearchDTO(params);

        params.setUserIdList(params.getUserIdList() != null ? params.getUserIdList() : new ArrayList());
        List<Long> userIdList = params.getUserIdList();
        userIdList.addAll(setIdNumUserIdList(params.getIdNum()));
        employeePageSearchDTO.setUserIdList(userIdList);


        PageResultComDTO<EmployeePageResultDTO> employeePage = getEmployeePage(employeePageSearchDTO);
        IPage<CertUserResponse> certUserResponsePage = getCertUserResponsePage(employeePage);
        setPhoneAndIdNumInfoByCertUserResponse(certUserResponsePage.getRecords());
        setAwardedResponse(certUserResponsePage.getRecords(), params.getCertId());
        return certUserResponsePage;
    }

    private void setAwardedResponse(List<CertUserResponse> records, Long certId) {
        if (CollUtil.isNotEmpty(records) && certId != null) {
            List<Long> userIdList = records.stream().map(CertUserResponse::getUserId).collect(Collectors.toList());
            List<AwardRecord> awardRecordList = awardRecordService.list(new LambdaQueryWrapper<AwardRecord>().eq(AwardRecord::getCertId, certId).in(AwardRecord::getUserId, userIdList).eq(AwardRecord::getEnabled, Constants.DB_TRUE));
            if (CollUtil.isNotEmpty(awardRecordList)) {
                List<Long> awardUserIdList = awardRecordList.stream().map(AwardRecord::getUserId).collect(Collectors.toList());
                for (CertUserResponse record : records) {
                    if (awardUserIdList.contains(record.getUserId())) {
                        record.setAwarded(Constants.DB_TRUE);
                    }
                }
            }
        }
    }

    @Override
    public void certPostRetry(IdRequest request) {
        List<AwardRecord> awardRecordList = awardRecordService.list(new LambdaQueryWrapper<AwardRecord>().eq(AwardRecord::getId, request.getId()));
        certPostAwardRecordList(awardRecordList);
    }

    @Override
    public void awardBatchEnable(BatchFlagRequest batchFlagRequest) {
        List<Long> idList = batchFlagRequest.getIdList();
        Integer flag = batchFlagRequest.getFlag();
        if (CollUtil.isEmpty(idList)) {
            throw new ServiceException("请选中");
        }
        List<AwardRecord> awardRecordList = new ArrayList<>();
        for (Long id : idList) {
            AwardRecord awardRecord = new AwardRecord();
            awardRecord.setId(id);
            awardRecord.setEnabled(flag);
            awardRecordList.add(awardRecord);
        }
        awardRecordService.updateBatchById(awardRecordList);

    }

    @Override
    @Async
    public void certWaterPost(BatchFlagRequest batchFlagRequest, Long orgId) {
        SecurityContextHolder.setOrgId(orgId.toString());
        if (Constants.DB_FAIL.equals(batchFlagRequest.getFlag())) {
            List<Long> idList = batchFlagRequest.getIdList();
            List<Poster> waterPosterList = getWaterPosterList(idList);
            Map<Long, String> awardPostUrlMap = certPostService.addCertPost(waterPosterList);
            List<AwardRecord> awardRecordUpdateList = getAwardRecordUpdateList(awardPostUrlMap);
            awardRecordService.updateBatchById(awardRecordUpdateList);
        }
    }

    @Override
    public IPage<CertUserResponse> getStudentPage(CertStudentPageRequest params) {
        params.setIdNumber(params.getIdNum());
        if (remoteStudentService.pageWithStaff(params).getRemoteData() != null) {
            PageResultComDTO<StudentDTO> studentDTOPageResultComDTO = remoteStudentService.pageWithStaff(params).getRemoteData();
            Page<CertUserResponse> certUserResponseIPage = new Page<>();
            BeanUtil.copyProperties(studentDTOPageResultComDTO, certUserResponseIPage);
            if (CollUtil.isNotEmpty(studentDTOPageResultComDTO.getRecords())) {
                certUserResponseIPage.setRecords(studentDTOPageResultComDTO.getRecords().stream().map(i -> {
                    CertUserResponse certUserResponse = new CertUserResponse();
                    certUserResponse.setId(i.getUserId());
                    certUserResponse.setUserId(i.getUserId());
                    certUserResponse.setStudentId(i.getId());
                    certUserResponse.setUserName(i.getRealName());
                    certUserResponse.setPhone(i.getPhone());
                    certUserResponse.setIdNum(i.getIdNumber());
                    return certUserResponse;
                }).collect(Collectors.toList()));
                setAwardedResponse(certUserResponseIPage.getRecords(), params.getCertId());
            }
            return certUserResponseIPage;
        }
        return null;
    }

    private List<Poster> getWaterPosterList(List<Long> awardIdList) {
        List<AwardRecord> awardRecordWaterList = awardRecordService.listByIds(awardIdList);
        if (CollUtil.isNotEmpty(awardRecordWaterList)) {
            List<Poster> posters = new ArrayList<>();
            for (AwardRecord awardRecord : awardRecordWaterList) {
                Poster poster = new Poster();
                poster.setId(awardRecord.getId());
                List<AwardRecordDetail> awardRecordDetailList = awardRecordDetailService.list(new LambdaQueryWrapper<AwardRecordDetail>().eq(AwardRecordDetail::getAwardRecordId, awardRecord.getId()));

                AwardRecordDetail awardRecordBackGroundDetail = awardRecordDetailList.stream().filter(i -> CertTemplateConfigEnum.BACKGROUND_URL.getCode().equals(i.getCode())).collect(Collectors.toList()).get(0);
                Image image = JSON.parseObject(awardRecordBackGroundDetail.getCustomStyle(), Image.class);
                image.setUrl(awardRecord.getCertImageUrl());
                poster.setHeight(image.getHeight());
                poster.setWidth(image.getWidth());
                poster.setImages(ListUtil.toList(image));

                Text text = new Text();
                text.setColor("#AB2524");
                text.setX(582);
                text.setY(436);
                text.setText("作废");
                text.setFontSize(60);
                poster.setTexts(ListUtil.toList(text));

                posters.add(poster);
            }
            return posters;
        }
        return null;
    }

    private IPage<CertUserResponse> getCertUserResponsePage(PageResultComDTO<EmployeePageResultDTO> employeePage) {
        if (employeePage != null) {
            Page<CertUserResponse> certUserResponseIPage = new Page<>();
            BeanUtil.copyProperties(employeePage, certUserResponseIPage);
            List<EmployeePageResultDTO> records = employeePage.getRecords();
            if (CollUtil.isNotEmpty(records)) {
                List<CertUserResponse> certUserList = new ArrayList<>();
                for (EmployeePageResultDTO record : records) {
                    CertUserResponse certUserResponse = new CertUserResponse();
                    BeanUtil.copyProperties(record, certUserResponse);
                    certUserResponse.setUserName(record.getRealName());
                    certUserList.add(certUserResponse);
                }
                certUserResponseIPage.setRecords(certUserList);
            }
            return certUserResponseIPage;
        }
        return null;
    }

    private void setPhoneAndIdNumInfoByCertUserResponse(List<CertUserResponse> records) {
        if (CollUtil.isNotEmpty(records)) {
            List<Long> userIdList = records.stream().map(CertUserResponse::getUserId).collect(Collectors.toList());
            UserListDTO userListDTO = new UserListDTO();
            userListDTO.setIdList(userIdList);
            R<List<UserListResultDTO>> listR = remoteUserService.listUser(userListDTO);
            if (R.SUCCESS == listR.getCode()) {
                List<UserListResultDTO> data = listR.getData();
                Map<Long, List<UserListResultDTO>> userMap = data.stream().collect(Collectors.groupingBy(UserListResultDTO::getId));
                for (CertUserResponse record : records) {
                    if (userMap.containsKey(record.getUserId())) {
                        record.setIdNum(userMap.get(record.getUserId()).get(0).getIdNumber());
                        record.setPhone(userMap.get(record.getUserId()).get(0).getAccount());
                    }
                }
            }
        }
    }

    private EmployeePageSearchDTO setEmployeeInfoPageSearchDTO(CertUserPageRequest params) {
        EmployeePageSearchDTO employeePageSearchDTO = new EmployeePageSearchDTO();
        BeanUtil.copyProperties(params, employeePageSearchDTO);
        if (StrUtil.isNotBlank(params.getPhone())) {
            employeePageSearchDTO.setSearchContent(params.getPhone());
        }
        if (StrUtil.isNotBlank(params.getUserName())) {
            employeePageSearchDTO.setSearchContent(params.getUserName());
        }
        if (CollUtil.isNotEmpty(params.getDeptIdList())) {
            employeePageSearchDTO.setDeptIdList(params.getDeptIdList());
        }
        if (params.getRoleId() != null) {
            employeePageSearchDTO.setRoleId(params.getRoleId());
        }
        return employeePageSearchDTO;
    }

    private List<Long> setIdNumUserIdList(String idNum) {
        List<Long> userIdList = new ArrayList<>();
        if (StrUtil.isNotBlank(idNum)) {
            UserListFieldDTO userListFieldDTO = new UserListFieldDTO();
            userListFieldDTO.setIdNumber(idNum);
            List<UserListResultDTO> userListResultDTO = getUserListResultDTO(userListFieldDTO);
            if (CollUtil.isNotEmpty(userListResultDTO)) {
                userIdList.add(userListResultDTO.get(0).getId());
            }
        }
        return userIdList;
    }

    private PageResultComDTO<EmployeePageResultDTO> getEmployeePage(EmployeePageSearchDTO employeePageSearchDTO) {
        R<PageResultComDTO<EmployeePageResultDTO>> pageResult = remoteEmployeeService.page(employeePageSearchDTO);
        if (R.SUCCESS == pageResult.getCode()) {
            return pageResult.getData();
        }
        return null;
    }

    private void setAwardRecordUserInfo(IPage<AwardRecordPageResponse> page) {
        if (CollUtil.isNotEmpty(page.getRecords())) {
            List<AwardRecordPageResponse> awardRecordList = page.getRecords();
            List<Long> awardRecordIdList = awardRecordList.stream().map(AwardRecordPageResponse::getId).collect(Collectors.toList());
            Map<Long, Map<String, List<AwardRecordDetail>>> awardRecordDetailGroup = awardRecordDetailService.list(new LambdaQueryWrapper<AwardRecordDetail>().eq(AwardRecordDetail::getType, TemplateConfigTypeEnum.SEARCH_CONFIG.getId()).in(AwardRecordDetail::getAwardRecordId, awardRecordIdList)).stream().collect(Collectors.groupingBy(AwardRecordDetail::getAwardRecordId, Collectors.groupingBy(AwardRecordDetail::getCode)));
            awardRecordList.parallelStream().forEach(awardRecordPageResponse -> {
                if (awardRecordDetailGroup.containsKey(awardRecordPageResponse.getId())) {
                    Map<String, List<AwardRecordDetail>> awardRecordCodeGroup = awardRecordDetailGroup.get(awardRecordPageResponse.getId());
                    awardRecordPageResponse.setUserName(awardRecordCodeGroup.get(CertTemplateConfigEnum.USER_NAME.getCode()).get(0).getKeyValue());
                    awardRecordPageResponse.setIdNum(awardRecordCodeGroup.containsKey(CertTemplateConfigEnum.ID_NUM.getCode()) ? awardRecordCodeGroup.get(CertTemplateConfigEnum.ID_NUM.getCode()).get(0).getKeyValue() : null);
                    awardRecordPageResponse.setPhone(awardRecordCodeGroup.containsKey(CertSearchConfigEnum.PHONE.getCode()) ? awardRecordCodeGroup.get(CertSearchConfigEnum.PHONE.getCode()).get(0).getKeyValue() : null);
                }
                if (awardRecordPageResponse.getCreateTime().plusMinutes(10L).isBefore(LocalDateTime.now()) && awardRecordPageResponse.getCertImageUrl() == null) {
                    awardRecordPageResponse.setRetryAward(true);
                }
            });
        }
    }


    private List<AwardRecord> getAwardRecordUpdateList(Map<Long, String> awardPostUrlMap) {
        List<AwardRecord> awardRecordUpdateList = new ArrayList<>();
        for (Long awardRecordId : awardPostUrlMap.keySet()) {
            AwardRecord awardRecord = new AwardRecord();
            awardRecord.setId(awardRecordId);
            awardRecord.setCertImageUrl(awardPostUrlMap.get(awardRecordId));
            awardRecordUpdateList.add(awardRecord);
        }
        return awardRecordUpdateList;
    }

    private List<Poster> getPostList(Map<Long, List<AwardRecordDetail>> awardRecordDetailGroup) {
        List<Poster> posterList = new ArrayList<>();
        for (Long awardRecordId : awardRecordDetailGroup.keySet()) {
            List<AwardRecordDetail> awardRecordDetails = awardRecordDetailGroup.get(awardRecordId);
            Poster poster = awardPostHandler.load(awardRecordDetails.stream().sorted(Comparator.comparing(AwardRecordDetail::getId).reversed()).collect(Collectors.toList()));
            poster.setId(awardRecordId);
            posterList.add(poster);

        }
        return posterList;
    }

    private List<UserListResultDTO> getUserListResultDTO(UserListFieldDTO userListFieldDTO) {
        R<List<UserListResultDTO>> listR = remoteUserService.listUserByField(userListFieldDTO);
        if (R.SUCCESS == listR.getCode()) {
            return listR.getData();
        }
        return null;
    }

//    private UserListFieldDTO getUserListFieldDTO(List<CommonConfigDetailLiteDTO> userInfoSearchList) {
//        UserListFieldDTO userListFieldDTO = new UserListFieldDTO();
//        for (CommonConfigDetailLiteDTO commonConfigDetailLiteDTO : userInfoSearchList) {
//            CertSearchConfigEnum certSearchConfigEnum = CertSearchConfigEnum.getEnumByCode(commonConfigDetailLiteDTO.getCode());
//            switch (certSearchConfigEnum) {
//                case USER_NAME:
//                    userListFieldDTO.setRealName(commonConfigDetailLiteDTO.getKeyValue());
//                    break;
//                case ID_NUM:
//                    userListFieldDTO.setIdNumber(commonConfigDetailLiteDTO.getKeyValue());
//                    break;
//                case PHONE:
//                    userListFieldDTO.setPhone(commonConfigDetailLiteDTO.getKeyValue());
//                    break;
//            }
//        }
//        return userListFieldDTO;
//    }


    @Override
    public List<CertDetailResponse> repetition(CertListRequest certDetailRequest) {
        List<Cert> list = baseMapper.selectList(new QueryWrapper<Cert>().eq("name", certDetailRequest.getName()));
        ArrayList<CertDetailResponse> certDetailResponses = new ArrayList<>();
        if (CollUtil.isNotEmpty(list)) {
            for (Cert cert : list) {
                CertDetailResponse certDetailResponse = new CertDetailResponse();
                certDetailResponse.setId(cert.getId());
                certDetailResponses.add(certDetailResponse);
            }
        }
        return certDetailResponses;
    }

    @Override
    public List<PersonCertDetailResponse> acquire(PersonCertDetailRequest personCertDetailRequest) {
        personCertDetailRequest.setUserId(SecurityContextHolder.getUserId());
        List<AwardRecord> list = awardRecordService.list(new QueryWrapper<AwardRecord>().
                eq("user_id", personCertDetailRequest.getUserId()).
                eq("cert_id", personCertDetailRequest.getCertId()).
                eq(personCertDetailRequest.getSourceId() != null, "source_id", personCertDetailRequest.getSourceId()));
        List<PersonCertDetailResponse> personCertDetailResponseArrayList = null;
        if (CollUtil.isNotEmpty(list)) {
//            personCertDetailResponseArrayList = BeanUtil.copyToList(list, PersonCertDetailResponse.class);

            personCertDetailResponseArrayList = new ArrayList<>();
            for (AwardRecord awardRecord : list) {
                PersonCertDetailResponse personCertDetailResponse = new PersonCertDetailResponse();
                BeanUtils.copyProperties(awardRecord, personCertDetailResponse);
                personCertDetailResponse.setEnabled(awardRecord.getEnabled() == 1 ? true : false);
                personCertDetailResponseArrayList.add(personCertDetailResponse);
            }
        }
        return personCertDetailResponseArrayList;
    }

    @Override
    public List<AwardRecordDetailResponse> grant(IdRequest idRequest) {
        List<AwardRecord> list = awardRecordService.list(new LambdaQueryWrapper<AwardRecord>().eq(AwardRecord::getCertId, idRequest.getId()));
        if (CollUtil.isNotEmpty(list)) {
            List<AwardRecordDetailResponse> awardRecordDetailResponses = new ArrayList<>();
            BeanUtil.copyProperties(list, awardRecordDetailResponses);
            return awardRecordDetailResponses;
        }
        return null;
    }

    @Override
    public IPage<CertPageResponse> myPageV2(CertPageRequest params) {
        return baseMapper.myPageV2(params.createMpPage(), params);
    }

    @Override
    public List<PersonCertDetailResponse> batchSelect(BatchGetCertRequest batchGetCertRequest) {
        return baseMapper.batchSelect(batchGetCertRequest);
    }
}

