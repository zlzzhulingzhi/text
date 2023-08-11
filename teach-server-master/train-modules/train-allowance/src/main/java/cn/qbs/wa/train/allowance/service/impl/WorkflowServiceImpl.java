package cn.qbs.wa.train.allowance.service.impl;

import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.train.allowance.entity.Workflow;
import cn.qbs.wa.train.allowance.entity.WorkflowNode;
import cn.qbs.wa.train.allowance.mapper.WorkflowMapper;
import cn.qbs.wa.train.allowance.mapper.WorkflowNodeMapper;
import cn.qbs.wa.train.allowance.pojo.workflow.*;
import cn.qbs.wa.train.allowance.service.WorkflowService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 流程定义(Workflow)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-13 13:58:46
 */
@Slf4j
@Service("workflowService")
public class WorkflowServiceImpl extends ServiceImpl<WorkflowMapper, Workflow> implements WorkflowService {

    @Resource
    WorkflowNodeMapper workflowNodeMapper;

    @Override
    public boolean add(WorkflowAddRequest params) {
        Long count = baseMapper.selectCount(Wrappers.<Workflow>lambdaQuery().eq(Workflow::getFlowCode, params.getFlowCode()));
        if(count> Constants.DB_FAIL){
            throw new ServiceException("已有同名流程代号");
        }
        Long count2 = baseMapper.selectCount(Wrappers.<Workflow>lambdaQuery().eq(Workflow::getFlowName, params.getFlowName()));
        if(count2> Constants.DB_FAIL){
            throw new ServiceException("已有同名流程名称");
        }
        Workflow workflow = new Workflow();
        BeanUtils.copyProperties(params, workflow);
        return this.save(workflow);
    }

    @Override
    public IPage<WorkflowPageResponse> page(WorkflowPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public WorkflowDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(WorkflowUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        Workflow workflow1 = this.lambdaQuery().eq(Workflow::getFlowCode,params.getFlowCode()).one();
        if(workflow1!=null && !params.getId().equals(workflow1.getId())){
            throw new ServiceException("已有同名流程代号");
        }
        workflow1= this.lambdaQuery().eq(Workflow::getFlowName,params.getFlowName()).one();
        if(workflow1!=null && !params.getId().equals(workflow1.getId())){
            throw new ServiceException("已有同名流程名称");
        }
        Workflow workflow=new Workflow();
        BeanUtils.copyProperties(params, workflow);
        return this.updateById(workflow);
    }

    @Override
    public boolean updateBatch(WorkflowUpdateBatchRequest params) {
        List<Workflow> workflowList = new ArrayList<>();
        for (Long id : params.getIds()) {
            if (id == null) {
                throw new IllegalParamsException("ID不能为空！");
            }
            Workflow workflow = new Workflow();
            workflow.setId(id);
            workflow.setEnabled(params.getEnabled());
            workflowList.add(workflow);
        }
        return this.updateBatchById(workflowList);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        for (Long id:idList) {
            Workflow workflow=this.getById(id);
            Long count=workflowNodeMapper.selectCount(Wrappers.<WorkflowNode>lambdaQuery().
                    eq(WorkflowNode::getWorkflowId,workflow.getId()));
            if(count> Constants.DB_FAIL){
                throw new ServiceException("已有步骤无法删除流程");
            }
        }
        return this.removeByIds(idList);
    }
    
}

