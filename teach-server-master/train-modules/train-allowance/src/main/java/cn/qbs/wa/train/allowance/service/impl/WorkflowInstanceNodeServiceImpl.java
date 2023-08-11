package cn.qbs.wa.train.allowance.service.impl;

import cn.qbs.wa.train.allowance.entity.WorkflowInstanceNode;
import cn.qbs.wa.train.allowance.mapper.WorkflowInstanceNodeMapper;
import cn.qbs.wa.train.allowance.service.WorkflowInstanceNodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 流程节点实例(WorkflowInstanceNode)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-13 13:56:10
 */
@Slf4j
@Service("workflowInstanceNodeService")
public class WorkflowInstanceNodeServiceImpl extends ServiceImpl<WorkflowInstanceNodeMapper, WorkflowInstanceNode> implements WorkflowInstanceNodeService {

}

