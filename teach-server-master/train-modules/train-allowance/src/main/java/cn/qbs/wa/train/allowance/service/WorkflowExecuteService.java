package cn.qbs.wa.train.allowance.service;

import cn.qbs.wa.train.allowance.pojo.workflowexecute.WorkflowEndRequest;
import cn.qbs.wa.train.allowance.pojo.workflowexecute.WorkflowExecuteRequest;
import cn.qbs.wa.train.allowance.pojo.workflowexecute.WorkflowResult;
import cn.qbs.wa.train.allowance.pojo.workflowexecute.WorkflowStartResult;

/**
 * 工作流执行接口
 */
public interface WorkflowExecuteService {

    /**
     * 启动流程
     * @param flowCode 流程代号
     * @return 启动结果
     */
    WorkflowStartResult start(String flowCode);

    /**
     * 启动流程
     * @param flowCode  流程代号
     * @param processNo 流程实例编号
     * @return 启动结果
     */
    WorkflowStartResult start(String flowCode, String processNo);

    /**
     * 获取流程实例编号
     * @param flowCode 流程代号
     * @return 流程实例编号
     */
    String generateProcessNo(String flowCode);

    /**
     * 返回上一步流程
     * @param executeRequest 流程审批请求
     * @return 结果
     */
    WorkflowResult previous(WorkflowExecuteRequest executeRequest);

    /**
     * 执行下一步流程, 当返回结果中下一流程节点为空则说明流程结束, ended 流程是否结束标识(1-结束，0-未结束)
     * @param executeRequest 流程审批请求
     * @return 结果
     */
    WorkflowResult next(WorkflowExecuteRequest executeRequest);

    /**
     * 结束流程
     * @param executeRequest 流程审批请求
     * @return 结果
     */
    Boolean end(WorkflowEndRequest executeRequest);

}
