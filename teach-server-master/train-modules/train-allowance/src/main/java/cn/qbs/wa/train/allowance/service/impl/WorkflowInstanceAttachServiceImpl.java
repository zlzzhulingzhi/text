package cn.qbs.wa.train.allowance.service.impl;

import cn.qbs.wa.train.allowance.entity.WorkflowInstanceAttach;
import cn.qbs.wa.train.allowance.mapper.WorkflowInstanceAttachMapper;
import cn.qbs.wa.train.allowance.service.WorkflowInstanceAttachService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 流程实例附件(WorkflowInstanceAttach)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-13 13:56:09
 */
@Slf4j
@Service("workflowInstanceAttachService")
public class WorkflowInstanceAttachServiceImpl extends ServiceImpl<WorkflowInstanceAttachMapper, WorkflowInstanceAttach> implements WorkflowInstanceAttachService {

}

