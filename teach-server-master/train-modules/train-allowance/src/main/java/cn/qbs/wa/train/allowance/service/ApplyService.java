package cn.qbs.wa.train.allowance.service;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.train.allowance.entity.Apply;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyAddRequest;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyByWorkflowNodeDTO;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyPageResponse;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyRequest;
import cn.qbs.wa.train.allowance.pojo.approve.ApplySettlePageRequest;
import cn.qbs.wa.train.allowance.pojo.workflownode.WorkflowInstanceNodeResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 申请表(Apply)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-14 15:05:37
 */
public interface ApplyService extends IService<Apply> {

    Long settleAdd(ApplyAddRequest request);

    IPage<ApplyPageResponse> page(IPage<?> page, ApplyRequest applyRequest);

    IPage<ApplyPageResponse> pageSettleApply(ApplySettlePageRequest request);

    /**
     * 提交申请，进行审核
     *
     * @param applyId
     * @return
     */
    Boolean submit(Long applyId);

    /**
     * @param applyId 申请单ID
     * @param isDel   删除校验
     */
    void checkApplyStatus(Long applyId, boolean isDel);

    boolean removeApply(Long id);

    ApplyByWorkflowNodeDTO selectApplyByWorkflowNode(IdRequest request);

    List<WorkflowInstanceNodeResponse> selectApproverInfo(List<WorkflowInstanceNodeResponse> workflowInstanceNodeResponses);

    IPage<ApplyPageResponse> selectCreateBy(IPage<ApplyPageResponse> page);
}
