package cn.qbs.wa.teach.exam.admin.service.impl;

import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdOrgRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.exam.admin.service.PaperService;
import cn.qbs.wa.teach.question.api.RemotePaperService;
import cn.qbs.wa.teach.question.api.pojo.DTO.paper.BasicPaper;
import cn.qbs.wa.teach.question.api.pojo.DTO.paper.PaperDetailDTO;
import cn.qbs.wa.teach.question.api.pojo.DTO.paper.UpdatePaperEditableRequest;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @Author zcm
 * @Date 2021-12-24 10:32
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaperServiceImpl implements PaperService {

    private final RemotePaperService remotePaperService;

    @Override
    public PaperDetailDTO getPaperDetail(Long paperId) {
        IdOrgRequest request = new IdOrgRequest();
        request.setId(paperId);
        Long orgId = SecurityContextHolder.getOrgId();
        request.setOrgId(orgId);
        R<PaperDetailDTO> r = remotePaperService.detail(request);
        if (r != null && r.isOk()) {
            PaperDetailDTO paperDetail = r.getData();
            return paperDetail;
        }
        return null;
    }

    @Override
    public boolean editable(Long paperId, boolean editable) {
        UpdatePaperEditableRequest updatePaperEditableRequest = new UpdatePaperEditableRequest(paperId, editable);
        R<Boolean> r = remotePaperService.editable(updatePaperEditableRequest);
        if (r != null && r.isOk()) {
            return r.getData();
        }
        return false;
    }

    @Override
    public List<BasicPaper> getBasicList(List<Long> paperIdList) {
        if (CollectionUtils.isNotEmpty(paperIdList)) {
            IdListRequest param = new IdListRequest();
            param.setIdList(paperIdList);
            param.setOrgId(SecurityContextHolder.getOrgId());
            R<List<BasicPaper>> r = remotePaperService.basicList(param);
            if (r != null && r.isOk()) {
                return r.getData();
            }
        }

        return null;
    }

}
