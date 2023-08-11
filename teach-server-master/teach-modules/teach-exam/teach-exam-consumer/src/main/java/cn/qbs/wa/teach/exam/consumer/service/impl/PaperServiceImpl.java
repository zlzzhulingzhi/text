package cn.qbs.wa.teach.exam.consumer.service.impl;

import cn.qbs.wa.teach.common.core.domain.IdOrgRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.exam.consumer.service.PaperService;
import cn.qbs.wa.teach.question.api.RemotePaperService;
import cn.qbs.wa.teach.question.api.pojo.DTO.paper.PaperDetailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @Author zcm
 * @Date 2022-01-02 11:13
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaperServiceImpl implements PaperService {

    private final RemotePaperService remotePaperService;

    @Override
    public PaperDetailDTO getPaperDetail(Long paperId, Long orgId) {
        IdOrgRequest request = new IdOrgRequest(paperId, orgId);
        R<PaperDetailDTO> r = remotePaperService.detail(request);
        if (r != null && r.isOk()) {
            PaperDetailDTO paperDetail = r.getData();
            return paperDetail;
        }
        return null;
    }

}
