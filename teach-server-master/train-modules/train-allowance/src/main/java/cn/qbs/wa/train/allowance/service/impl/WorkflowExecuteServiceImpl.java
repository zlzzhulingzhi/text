package cn.qbs.wa.train.allowance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.train.allowance.entity.WorkflowInstanceAttach;
import cn.qbs.wa.train.allowance.entity.WorkflowInstanceNode;
import cn.qbs.wa.train.allowance.entity.WorkflowNode;
import cn.qbs.wa.train.allowance.enums.FlowNodeStatusEnum;
import cn.qbs.wa.train.allowance.mapper.WorkflowNodeMapper;
import cn.qbs.wa.train.allowance.pojo.workflowexecute.*;
import cn.qbs.wa.train.allowance.service.WorkflowExecuteService;
import cn.qbs.wa.train.allowance.service.WorkflowInstanceAttachService;
import cn.qbs.wa.train.allowance.service.WorkflowInstanceNodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service("workflowExecuteService")
public class WorkflowExecuteServiceImpl implements WorkflowExecuteService {

    @Resource
    private Snowflake snowflake;

    @Resource
    private WorkflowNodeMapper workflowNodeMapper;

    @Resource
    private WorkflowInstanceNodeService workflowInstanceNodeService;

    @Resource
    private WorkflowInstanceAttachService workflowInstanceAttachService;

    @Override
    public WorkflowStartResult start(String flowCode) {
        // 分配流程实例编号
        String processNo = generateProcessNo(flowCode);
        return this.start(flowCode, processNo);
    }

    @Override
    public WorkflowStartResult start(String flowCode, String processNo) {
        // 根据流程代号查询流程节点
        List<WorkflowNode> nodeList = workflowNodeMapper.listByFlowCode(flowCode);
        if (CollUtil.isEmpty(nodeList)) {
            throw new ServiceException("编号：[" + flowCode + "]流程不存");
        }
        nodeList = nodeList.stream().sorted(Comparator.comparingInt(WorkflowNode::getSort)).collect(Collectors.toList());

        Long workflowId = nodeList.get(0).getWorkflowId();
        int size = nodeList.size();
        List<WorkflowInstanceNode> instanceList = new ArrayList<>(size);
        if (StrUtil.isBlank(processNo)) {
            // 分配流程实例编号
            processNo = generateProcessNo(flowCode);
        }
        long key = snowflake.nextId();
        // 初始化流程实例步骤
        for (int i = 0; i < size; i++) {
            WorkflowNode node = nodeList.get(i);
            WorkflowInstanceNode instanceNode = new WorkflowInstanceNode();
            instanceNode.setNodeCode(node.getNodeCode());
            instanceNode.setNodeName(node.getNodeName());
            instanceNode.setManagerType(node.getManagerType());
            instanceNode.setManagerRef(node.getManagerRef());
            long curId = key + i;
            instanceNode.setId(curId);
            instanceNode.setPrevId(0 == i ? null : curId - 1);
            instanceNode.setNextId(i == size - 1 ? null : curId + 1);
            instanceNode.setProcessNo(processNo);
            instanceNode.setWorkflowId(workflowId);
            if (0 == i) {
                instanceNode.setStatus(FlowNodeStatusEnum.IN_PROGRESS.getCode());
            } else {
                instanceNode.setStatus(FlowNodeStatusEnum.NOT_START.getCode());
            }
            instanceList.add(instanceNode);
        }
        workflowInstanceNodeService.saveBatch(instanceList);

        WorkflowStartResult workflowStartResult = new WorkflowStartResult();
        workflowStartResult.setProcessNo(processNo);
        workflowStartResult.setCurNodeId(key);
        workflowStartResult.setCurNodeCode(nodeList.get(0).getNodeCode());

        return workflowStartResult;
    }

    @Override
    public String generateProcessNo(String flowCode) {
        // 生成流程实例编号 流程代号 + 当前时间
        return flowCode + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }

    @Override
    public WorkflowResult previous(WorkflowExecuteRequest executeRequest) {
        // 获取当前流程所处节点
        WorkflowInstanceNode curFlowNode = getCurrentFlowNode(executeRequest.getProcessNo());
        // 检查节点状态
        checkFlowNode(curFlowNode);
        // 将当前流程实例节点改为 未开始 状态，激活上一流程节点
        curFlowNode.setStatus(FlowNodeStatusEnum.NOT_START.getCode());
        Long prevId = curFlowNode.getPrevId();
        if (prevId == null) {
            throw new ServiceException("当前流程无上一流程节点");
        }
        WorkflowInstanceNode prevFlowNode = new WorkflowInstanceNode();
        prevFlowNode.setId(prevId);
        prevFlowNode.setStatus(FlowNodeStatusEnum.IN_PROGRESS.getCode());
        // 更新流程节点状态
        workflowInstanceNodeService.updateBatchById(Arrays.asList(curFlowNode, prevFlowNode));

        return new WorkflowResult(prevId, Constants.DB_FAIL);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public WorkflowResult next(WorkflowExecuteRequest executeRequest) {
        // 获取当前流程所处节点
        String processNo = executeRequest.getProcessNo();
        WorkflowInstanceNode curFlowNode = getCurrentFlowNode(processNo);
        // 检查节点状态
        checkFlowNode(curFlowNode);
        Long nextId = curFlowNode.getNextId();
        WorkflowResult workflowResult = new WorkflowResult();
        if (nextId == null) {
            // 当前节点下一个节点为空, 表示流程结束
            WorkflowEndRequest workflowEndRequest = BeanUtil.copyProperties(executeRequest, WorkflowEndRequest.class);
            workflowEndRequest.setPass(Boolean.TRUE);
            this.end(workflowEndRequest);
            workflowResult.setEnded(Constants.DB_TRUE);
        } else {
            // 将当前流程实例节点改为 已通过 状态，激活下一流程节点
            curFlowNode.setStatus(FlowNodeStatusEnum.PASS.getCode());
            curFlowNode.setComment(executeRequest.getComment());
            WorkflowInstanceNode next = new WorkflowInstanceNode();
            next.setId(nextId);
            next.setStatus(FlowNodeStatusEnum.IN_PROGRESS.getCode());
            // 更新流程节点状态
            workflowInstanceNodeService.updateBatchById(Arrays.asList(curFlowNode, next));

            // 保存附件信息
            saveAttach(processNo, curFlowNode.getId(), executeRequest.getAttachList());

            workflowResult.setNextNodeId(nextId);
            workflowResult.setEnded(Constants.DB_FAIL);
        }
        return workflowResult;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean end(WorkflowEndRequest executeRequest) {
        // 获取当前流程所处节点
        String processNo = executeRequest.getProcessNo();
        WorkflowInstanceNode curFlowNode = getCurrentFlowNode(processNo);
        // 检查节点状态
        checkFlowNode(curFlowNode);
        WorkflowInstanceNode updater = new WorkflowInstanceNode();
        updater.setId(curFlowNode.getId());
        updater.setComment(executeRequest.getComment());
        curFlowNode.setComment(executeRequest.getComment());
        if (executeRequest.getPass()) {
            curFlowNode.setStatus(FlowNodeStatusEnum.PASS.getCode());
        } else {
            curFlowNode.setStatus(FlowNodeStatusEnum.NOT_PASS.getCode());
        }
        workflowInstanceNodeService.updateById(curFlowNode);

        // 保存附件信息
        saveAttach(processNo, curFlowNode.getId(), executeRequest.getAttachList());
        return Boolean.TRUE;
    }

    /**
     * 根据流程实例编号获取当前流程所处节点
     * @param processNo 流程实例编号
     * @return 当前流程所处节点
     */
    private WorkflowInstanceNode getCurrentFlowNode(String processNo) {
        return workflowInstanceNodeService.lambdaQuery()
                .select(WorkflowInstanceNode::getId, WorkflowInstanceNode::getPrevId, WorkflowInstanceNode::getNextId, WorkflowInstanceNode::getStatus, WorkflowInstanceNode::getNodeName)
                .eq(WorkflowInstanceNode::getProcessNo, processNo)
                .eq(WorkflowInstanceNode::getStatus, FlowNodeStatusEnum.IN_PROGRESS.getCode())
                .one();
    }

    /**
     * 保存附件信息
     *
     * @param processNo      流程实例编号
     * @param instanceNodeId 流程节点ID
     * @param attachList     附件
     */
    private void saveAttach(String processNo, Long instanceNodeId, List<WorkflowNodeAttach> attachList) {
        if (CollUtil.isNotEmpty(attachList)) {
            List<WorkflowInstanceAttach> instanceAttachList = attachList.stream().map(a -> {
                WorkflowInstanceAttach attach = BeanUtil.copyProperties(a, WorkflowInstanceAttach.class);
                attach.setProcessNo(processNo);
                attach.setInstanceNodeId(instanceNodeId);
                return attach;
            }).collect(Collectors.toList());
            workflowInstanceAttachService.saveBatch(instanceAttachList);
        }
    }

    /**
     * 检查节点状态
     */
    private void checkFlowNode(WorkflowInstanceNode curFlowNode) {
        if (curFlowNode == null) {
            throw new ServiceException("流程节点不存在");
        }
        List<Integer> finish = Arrays.asList(FlowNodeStatusEnum.PASS.getCode(), FlowNodeStatusEnum.NOT_PASS.getCode());
        // 若当前节点已完成，报错提示
        if (finish.contains(curFlowNode.getStatus())) {
            throw new ServiceException(curFlowNode.getNodeName() + " 已完成，请刷新查看");
        }
    }

}
