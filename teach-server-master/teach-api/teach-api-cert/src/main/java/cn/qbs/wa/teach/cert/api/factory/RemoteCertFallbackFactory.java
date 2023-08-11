package cn.qbs.wa.teach.cert.api.factory;


import cn.qbs.wa.teach.cert.api.RemoteCertService;
import cn.qbs.wa.teach.cert.api.pojo.DTO.cert.*;
import cn.qbs.wa.teach.cert.api.pojo.DTO.cert.awardRecord.*;
import cn.qbs.wa.teach.common.core.domain.BatchFlagRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RemoteCertFallbackFactory implements FallbackFactory<RemoteCertService> {
    @Override
    public RemoteCertService create(Throwable cause) {
        return new RemoteCertService () {
            @Override
            public R<CertDetailResponseDTO> add(CertAddRequestDTO params) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<PageResultComDTO<CertPageResponseDTO>> page(CertPageRequestDTO params) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<CertDetailResponseDTO> detail(IdRequest request) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<Boolean> update(CertUpdateRequestDTO params) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R batchEnable(BatchFlagRequest batchFlagRequest) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<List<CertDetailResponseDTO>> repetition(CertListRequestDTO certDetailRequest) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<PageResultComDTO<AwardRecordPageResponseDTO>> awardRecordPage(AwardRecordPageRequestDTO recordPageRequest) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<List<PersonCertDetailResponseDTO>> acquire(PersonCertDetailRequestDTO personCertDetailRequest) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R award(CertAwardRequestDTO certAwardRequest) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<List<AwardRecordDetailResponseDTO>> grant(IdRequest idRequest) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<List<PersonCertDetailResponseDTO>> batchSelect(BatchGetCertRequestDTO batchGetCertRequestDTO) {
                return R.fail("服务暂无法访问");
            }
        };
    }
}
