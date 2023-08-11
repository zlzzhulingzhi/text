package cn.qbs.wa.teach.cert.service;

import cn.qbs.wa.teach.cert.entity.Cert;
import cn.qbs.wa.teach.cert.pojo.awardrecord.AwardRecordDetailResponse;
import cn.qbs.wa.teach.cert.pojo.awardrecord.AwardRecordPageRequest;
import cn.qbs.wa.teach.cert.pojo.awardrecord.AwardRecordPageResponse;
import cn.qbs.wa.teach.cert.pojo.cert.*;
import cn.qbs.wa.teach.common.core.domain.BatchFlagRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 证书(Cert)表服务接口
 *
 * @author makejava
 * @since 2022-01-19 19:19:21
 */
public interface CertService extends IService<Cert> {

    /**
     * 新增证书
     * @param params
     * @return
     */
    Cert add(CertAddRequest params);

    /**
     * 分页查询证书
     * @param params
     * @return
     */
    IPage<CertPageResponse> page(CertPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    CertDetailResponse detail(Serializable id);

    /**
     * 更新证书
     * @param params
     * @return
     */
    boolean update(CertUpdateRequest params);

    /**
     * 删除证书
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    void batchEnabled(Integer flag, List<Long> idList);

    List<CertUserTreeResponse> getUserList(CertUserListRequest params);

    List<CertUserResponse> getExamUserList(CertExamUserListRequest params);

    IPage<CertPageResponse> myPage(CertPageRequest params);

    List<CertListResponse> mySelectList(CertListRequest params);

    MyCertSearchResponse myCertSearch(MyCertSearchRequest params);

    void certPost(CertAwardRequest certAwardRequest);

    void batchEnable(BatchFlagRequest batchFlagRequest);

    void award(CertAwardRequest certAwardRequest);

    IPage<AwardRecordPageResponse> awardRecordPage(AwardRecordPageRequest recordPageRequest);

    IPage<CertUserResponse> getUserPage(CertUserPageRequest params);

    void certPostRetry(IdRequest request);

    void awardBatchEnable(BatchFlagRequest batchFlagRequest);

    void certWaterPost(BatchFlagRequest batchFlagRequest, Long orgId);

    IPage<CertUserResponse> getStudentPage(CertStudentPageRequest searchDTO);

    List<CertDetailResponse> repetition(CertListRequest certDetailRequest);

    List<PersonCertDetailResponse> acquire(PersonCertDetailRequest personCertDetailRequest);

    List<AwardRecordDetailResponse> grant(IdRequest idRequest);

    IPage<CertPageResponse> myPageV2(CertPageRequest params);

    List<PersonCertDetailResponse> batchSelect(BatchGetCertRequest batchGetCertRequest);

}


