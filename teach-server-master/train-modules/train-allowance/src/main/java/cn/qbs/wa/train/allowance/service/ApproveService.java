package cn.qbs.wa.train.allowance.service;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.train.allowance.entity.Apply;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyPageRequest;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyPageResponse;
import cn.qbs.wa.train.allowance.pojo.approve.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.protobuf.ServiceException;

public interface ApproveService {

    IPage<ApplyPageResponse> pageSettleApply(ApplySettlePageRequest request);

    ApplySettleDetailResponse detailSettleApply(Long applyId);

    /**
     * 公共审批接口
     *
     * @param request
     * @return
     * @throws ServiceException
     */
    Apply approveApply(ApproveRequest request) throws ServiceException;

    Apply approveInvalidatedApply(ApproveRequest request) throws ServiceException;

    IPage<ApplySettleClassroomPageResponse> pageSettleClassroomApply(ApplyPageRequest request);

    ApplySettleClassroomDetailResponse detailSettleClassroomApply(Long id);

    IPage<ApplyPageResponse> pageV2(ApplyPageRequest request);

    IPage<ApplyPageResponse> pageRecord(ApplyPageRequest request);

    boolean approveSettleApplyAfter(Apply apply);

    ApproveByWorkflowNodeDTO selectApplyByWorkflowNode(IdRequest request);

    Long selectQualificationCount();

    AllowanceAndActivityCount selectAllowanceAndActivityCount();

    IPage<ApplyPageResponse> pageApproveApply(ApprovePageRequest request);

    Long selectClassroomCount();

}
