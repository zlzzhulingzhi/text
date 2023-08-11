package cn.qbs.wa.teach.cert.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.cert.entity.AwardRecord;
import cn.qbs.wa.teach.cert.entity.AwardRecordBusiness;
import cn.qbs.wa.teach.cert.entity.AwardRecordDetail;
import cn.qbs.wa.teach.cert.entity.StandardTemplateConfig;
import cn.qbs.wa.teach.cert.enums.CertSearchConfigEnum;
import cn.qbs.wa.teach.cert.enums.CertTemplateConfigEnum;
import cn.qbs.wa.teach.cert.enums.TemplateConfigTypeEnum;
import cn.qbs.wa.teach.cert.mapper.AwardRecordMapper;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.organization.api.RemoteStudentService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentSearchDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/1/22 10:35
 */
@Component
public class AwardHandler {


    @Data
    public class CertRuleCount {
        Long count;

    }

    @Data
    public class CertRuleNum {
        String certNum;
    }


    private String examInfoTemplate = "你通过了{}考试，成绩{}分";

    @Autowired
    AwardRecordMapper awardRecordMapper;

    @Autowired
    RemoteStudentService remoteStudentService;


    public List<AwardRecordDetail> load(List<AwardRecord> awardRecordList, List<StandardTemplateConfig> certTemplateConfigList, List<AwardRecordBusiness> awardRecordBusinessList) {
        Map<Long, StudentDTO> studentMap = getStudentMap(awardRecordList);
        List<AwardRecordDetail> recordDetailList = new ArrayList<>();
        CertRuleCount certRuleCount = new CertRuleCount();
        for (AwardRecord awardRecord : awardRecordList) {
            CertRuleNum certRuleNum = new CertRuleNum();
            for (StandardTemplateConfig standardTemplateConfig : certTemplateConfigList) {
                AwardRecordDetail awardRecordDetail = new AwardRecordDetail();
                TemplateConfigTypeEnum templateConfigTypeEnum = TemplateConfigTypeEnum.getEnumById(standardTemplateConfig.getType());
                switch (templateConfigTypeEnum) {
                    case CERT_CONFIG:
                        setCertConfig(awardRecord, standardTemplateConfig, awardRecordDetail, studentMap, awardRecordBusinessList, certRuleCount, certRuleNum);
                        break;
                    case SEARCH_CONFIG:
                        setSearchConfig(awardRecord, standardTemplateConfig, awardRecordDetail, studentMap, certRuleNum);
                        break;
                    default:
                        break;
                }
                awardRecordDetail.setAwardRecordId(awardRecord.getId());
                awardRecordDetail.setId(null);
                recordDetailList.add(awardRecordDetail);
            }

        }
        return recordDetailList;
    }

    private void setSearchConfig(AwardRecord awardRecord, StandardTemplateConfig standardTemplateConfig, AwardRecordDetail awardRecordDetail, Map<Long, StudentDTO> userMap, CertRuleNum certRuleNum) {
        //这里只需要实例化用户信息
        CertSearchConfigEnum configEnum = CertSearchConfigEnum.getEnumByCode(standardTemplateConfig.getCode());
        BeanUtil.copyProperties(standardTemplateConfig, awardRecordDetail);
        switch (configEnum) {
            case USER_NAME:
                if (userMap.containsKey(awardRecord.getUserId())) {
                    awardRecordDetail.setKeyValue(userMap.get((awardRecord.getUserId())).getRealName());
                }
                break;
            case ID_NUM:
                if (userMap.containsKey(awardRecord.getUserId())) {
                    awardRecordDetail.setKeyValue(userMap.get((awardRecord.getUserId())).getIdNumber());
                }
                break;
            case PHONE:
                if (userMap.containsKey(awardRecord.getUserId())) {
                    awardRecordDetail.setKeyValue(userMap.get((awardRecord.getUserId())).getAccount());
                }
                break;
            case CERT_NUM:
                awardRecordDetail.setKeyValue(certRuleNum.getCertNum());
            default:
                break;
        }
    }

    private void setCertConfig(AwardRecord awardRecord, StandardTemplateConfig standardTemplateConfig, AwardRecordDetail awardRecordDetail, Map<Long, StudentDTO> userMap, List<AwardRecordBusiness> awardRecordBusinessList, CertRuleCount certRuleCount, CertRuleNum certRuleNum) {
        CertTemplateConfigEnum certTemplateConfigEnum = CertTemplateConfigEnum.getEnumByCode(standardTemplateConfig.getCode());
        BeanUtil.copyProperties(standardTemplateConfig, awardRecordDetail);
        Map<Long, AwardRecordBusiness> recordBusinessMap = getRecordBusinessMap(awardRecordBusinessList);
        switch (certTemplateConfigEnum) {
            case CERT_NAME:
                awardRecordDetail.setKeyValue(standardTemplateConfig.getKeyValue());
                break;
            case USER_NAME:
                if (userMap.containsKey(awardRecord.getUserId())) {
                    awardRecordDetail.setKeyValue(userMap.get((awardRecord.getUserId())).getRealName());
                }
                break;
            case HEAD_IMG:
                if (userMap.containsKey(awardRecord.getUserId())) {
                    awardRecordDetail.setKeyValue(userMap.get((awardRecord.getUserId())).getHeadImgUrl());
                }
                break;
            case ID_NUM:
                if (userMap.containsKey(awardRecord.getUserId())) {
                    awardRecordDetail.setKeyValue(userMap.get((awardRecord.getUserId())).getIdNumber());
                }
                break;
            case EXAM_INFO:
                if (recordBusinessMap.containsKey(awardRecord.getId())) {
                    AwardRecordBusiness awardRecordBusiness = recordBusinessMap.get(awardRecord.getId());
                    awardRecordDetail.setKeyValue(StrUtil.format(examInfoTemplate, awardRecordBusiness.getBusinessName(), awardRecordBusiness.getBusinessContent()));
                }
                break;
            case CERT_NUM_RULE:
                String certNum = setCertNum(certRuleCount, standardTemplateConfig.getKeyValue(), awardRecord.getCertId());
                awardRecordDetail.setKeyValue(certNum);
                awardRecord.setCertNum(certNum);
                awardRecord.setCertPrefix(standardTemplateConfig.getKeyValue());
                certRuleNum.setCertNum(certNum);
                break;
            default:
                break;
        }
    }

    private String setCertNum(CertRuleCount certRuleCount, String certPrefix, Long certId) {
        Long count;
        if (certRuleCount.getCount() == null) {
            count = awardRecordMapper.selectCount(new LambdaQueryWrapper<AwardRecord>().eq(AwardRecord::getCertId, certId).isNotNull(AwardRecord::getCertNum)) + 1;
            certRuleCount.setCount(count);
        } else {
            count = certRuleCount.getCount() + 1;
            certRuleCount.setCount(count);
        }
        int length = count.toString().length() > 4 ? count.toString().length() : 4;
        return StrUtil.isNotBlank(certPrefix) ? certPrefix + String.format("%0" + length + "d", count) : String.format("%0" + length + "d", count);
    }

    private Map<Long, StudentDTO> getStudentMap(List<AwardRecord> awardRecordList) {
        List<Long> userIdList = awardRecordList.stream().map(AwardRecord::getUserId).collect(Collectors.toList());
        Map<Long, StudentDTO> studentDTOMap = new HashMap<>();
        StudentSearchDTO studentSearchDTO = new StudentSearchDTO();
        studentSearchDTO.setUserIds(userIdList);
        studentSearchDTO.setOrgId(SecurityContextHolder.getOrgId());
        if (CollUtil.isNotEmpty(remoteStudentService.list(studentSearchDTO).getRemoteData())) {
            studentDTOMap = remoteStudentService.list(studentSearchDTO).getRemoteData().stream().collect(Collectors.toMap(StudentDTO::getUserId, i -> i));
        }
        return studentDTOMap;
    }


    private Map<Long, AwardRecordBusiness> getRecordBusinessMap(List<AwardRecordBusiness> awardRecordBusinessList) {
        Map<Long, AwardRecordBusiness> recordBusinessMap = new HashMap<>();
        if (CollUtil.isNotEmpty(awardRecordBusinessList)) {
            recordBusinessMap = awardRecordBusinessList.stream().collect(Collectors.toMap(AwardRecordBusiness::getAwardRecordId, t -> t));
        }
        return recordBusinessMap;
    }


}
