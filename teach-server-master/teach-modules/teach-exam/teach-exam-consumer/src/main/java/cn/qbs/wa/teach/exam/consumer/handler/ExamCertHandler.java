package cn.qbs.wa.teach.exam.consumer.handler;

import cn.qbs.wa.teach.cert.api.RemoteCertService;
import cn.qbs.wa.teach.cert.api.pojo.DTO.cert.awardRecord.CertAwardRequestDTO;
import cn.qbs.wa.teach.cert.api.pojo.DTO.cert.awardRecord.CertUserResponseDTO;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdOrgRequest;
import cn.qbs.wa.teach.common.core.utils.StringUtils;

import cn.qbs.wa.teach.exam.common.enumerate.ExamConditionEnum;
import cn.qbs.wa.teach.exam.common.enumerate.SourceTypeEnum;
import cn.qbs.wa.teach.exam.common.pojo.ExamAndCertDetailResponse;
import cn.qbs.wa.teach.exam.common.pojo.TCertRuleDetailResponse;
import cn.qbs.wa.teach.exam.consumer.service.ExamService;
import cn.qbs.wa.teach.exam.consumer.service.ExamineeRecordService;
import cn.qbs.wa.teach.organization.api.RemoteStudentService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vieux
 * @version 1.0
 * @date 2022/4/22 14:18
 */
@Slf4j
@Component
public class ExamCertHandler {
    @Resource
    private RemoteStudentService remoteStudentService;

    @Resource
    private RemoteCertService remoteCertService;

    @Resource
    private ExamService examService;

    @Resource
    private ExamineeRecordService examineeRecordService;

    public void awardCert(Long examineeId,Long orgId) {
        if(examineeId == null){
            log.error("未获取到考生id");
        }
        SecurityContextHolder.setOrgId(orgId.toString());
        SecurityContextHolder.setSelectOrgId(orgId.toString());
        //查询考试的信息详情
        ExamAndCertDetailResponse examAndCertDetailResponse = examService.selectCertRuleByExamineeId(examineeId);
        if (examAndCertDetailResponse != null) {
            examAndCertDetailResponse.getTCertRuleDetailResponseList().forEach(f -> {
                ExamConditionEnum certConditionEnum = ExamConditionEnum.fromName(f.getRuleCode());
                if (certConditionEnum != null) {
                    switch (certConditionEnum) {
                        case PASS_EXAM:
                            log.info("开始判断是否成绩合格,发放通过考试的证书");
                            //判断是否存在考试成绩
                            if (examAndCertDetailResponse.getScore() == null) {
                                log.info("考试成绩为空，不发放通过考试的证书");
                                break;
                            }
                            if (examAndCertDetailResponse.getScore().compareTo(examAndCertDetailResponse.getPassScore()) >= 0) {
                                //发放通过考试的证书
                                log.info("考试通过, 发放证书");
                                extracted(examAndCertDetailResponse, f);
                            }
                            break;
                        case COMPLETE_TEST:
                            log.info("判断是否提交考试, 发放考试完成的证书");
                            //判断是否存在考试成绩
                            /*if (examAndCertDetailResponse.getScore() != null) {
                                log.info("成绩不为空, 直接发放完成考试的证书");
                                extracted(examAndCertDetailResponse, f);
                                break;
                            }*/
                            //查询考试的状态
                            Long count = examineeRecordService.selectCountByExamineeId(examineeId);
                            if (count > 0) {
                                log.info("考试已提交, 发放完成考试的证书");
                                extracted(examAndCertDetailResponse, f);
                            }
                            break;
                        default:
                    }
                }
            });
        }

    }

    private void extracted(ExamAndCertDetailResponse examAndCertDetailResponse, TCertRuleDetailResponse tCertRulePageResponse) {
        //查询执行者的基本信息, 颁发证书
        IdOrgRequest idRequest = new IdOrgRequest();
        idRequest.setId(examAndCertDetailResponse.getStudentId());
        idRequest.setOrgId(SecurityContextHolder.getSelectOrgId());
        StudentDTO data = remoteStudentService.detailNoTenant(idRequest).getData();
        log.info("查询学生的信息"+ JSON.toJSONString(data));
        CertUserResponseDTO certUserResponseDTO = new CertUserResponseDTO();
        if (StringUtils.isNotNull(data)) {
            certUserResponseDTO.setUserName(data.getRealName());
            certUserResponseDTO.setIdNum(data.getIdNumber());
            certUserResponseDTO.setPhone(data.getPhone());
            certUserResponseDTO.setUserId(data.getUserId());
            certUserResponseDTO.setStudentId(data.getId());
            CertAwardRequestDTO certAwardRequestDTO = new CertAwardRequestDTO();
            certAwardRequestDTO.setCertId(tCertRulePageResponse.getCertId());
            certAwardRequestDTO.setOrgId(SecurityContextHolder.getSelectOrgId());
            certAwardRequestDTO.setSourceMark("考试:" + examAndCertDetailResponse.getExamName());
            certAwardRequestDTO.setSourceId(examAndCertDetailResponse.getId());
            certAwardRequestDTO.setSourceType(SourceTypeEnum.EXAM.getType());
            List<CertUserResponseDTO> certUserResponseDTOList = new ArrayList<>();
            certUserResponseDTOList.add(certUserResponseDTO);
            certAwardRequestDTO.setUserList(certUserResponseDTOList);
            log.info("远程接口调用用户信息" + JSON.toJSONString(certUserResponseDTOList));
            log.info("调用发放证书接口");
            remoteCertService.award(certAwardRequestDTO);
        }
    }
}
