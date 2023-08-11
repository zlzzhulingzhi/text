package cn.qbs.wa.train.allowance.service.impl;

import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.train.allowance.entity.WorkflowNode;
import cn.qbs.wa.train.allowance.enums.ManageTypeEnum;
import cn.qbs.wa.train.allowance.mapper.WorkflowNodeMapper;
import cn.qbs.wa.train.allowance.pojo.workflownode.*;
import cn.qbs.wa.train.allowance.service.WorkflowNodeService;
import cn.qbs.wa.train.basic.api.RemoteTrainRoleService;
import cn.qbs.wa.train.basic.api.RemoteTrainUserService;
import cn.qbs.wa.train.basic.api.pojo.DTO.role.RoleDTO;
import cn.qbs.wa.train.basic.api.pojo.DTO.role.RoleRequestDTO;
import cn.qbs.wa.train.basic.api.pojo.DTO.user.UserListDTO;
import cn.qbs.wa.train.basic.api.pojo.DTO.user.UserListResultDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 流程节点(WorkflowNode)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-13 13:59:24
 */
@Slf4j
@Service("workflowNodeService")
public class WorkflowNodeServiceImpl extends ServiceImpl<WorkflowNodeMapper, WorkflowNode> implements WorkflowNodeService {

    @Resource
    RemoteTrainRoleService remoteTrainRoleService;

    @Resource
    RemoteTrainUserService remoteTrainUserService;

    @Override
    public boolean add(List<WorkflowNodeAddRequest> params) {
        List<WorkflowNode> workflowNodeList=new ArrayList<>();
        for (WorkflowNodeAddRequest param:params) {
            Long count = baseMapper.selectCount(Wrappers.<WorkflowNode>lambdaQuery().eq(WorkflowNode::getWorkflowId,param.getWorkflowId()).
                    eq(WorkflowNode::getNodeCode,param.getNodeCode()));
            if(count> Constants.DB_FAIL){
                throw new ServiceException("已有相同流程节点");
            }
            WorkflowNode workflowNode = new WorkflowNode();
            BeanUtils.copyProperties(param, workflowNode);
            workflowNodeList.add(workflowNode);
        }
        return this.saveBatch(workflowNodeList);
    }

    @Override
    public IPage<WorkflowNodePageResponse> page(WorkflowNodePageRequest params) {
        IPage<WorkflowNodePageResponse> workflowNodePageResponseIPage=baseMapper.page(params.createMpPage(), params);
        for (WorkflowNodePageResponse workflowNodePageResponse:workflowNodePageResponseIPage.getRecords()) {
            if(ManageTypeEnum.ROLE.getCode().equals(workflowNodePageResponse.getManagerType())){
                RoleRequestDTO roleRequestDTO=new RoleRequestDTO();
                roleRequestDTO.setCode(workflowNodePageResponse.getManagerRef());
                RoleDTO roleDTO=remoteTrainRoleService.getRole(roleRequestDTO).getRemoteData();
                if (roleDTO!=null){
                    workflowNodePageResponse.setPrincipalInfo(roleDTO.getName());
                }
            }
            if(ManageTypeEnum.USER.getCode().equals(workflowNodePageResponse.getManagerType())){
                List<Long> idList=new ArrayList<>();
                idList.add(Long.parseLong(workflowNodePageResponse.getManagerRef()));
                UserListDTO userListDTO=new UserListDTO();
                userListDTO.setIdList(idList);
                List<UserListResultDTO> userListResultDTOList=remoteTrainUserService.listUser(userListDTO).getRemoteData();
                if(userListResultDTOList!=null && !userListResultDTOList.isEmpty()){
                    workflowNodePageResponse.setPrincipalInfo(userListResultDTOList.get(Constants.DB_FAIL).getRealName());
                }
            }

        }
        return workflowNodePageResponseIPage;
    }

    @Override
    public WorkflowNodeDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(List<WorkflowNodeUpdateRequest> params) {
        List<WorkflowNode> workflowNodeList=new ArrayList<>();
        for (WorkflowNodeUpdateRequest param:params) {
            if (param.getId() == null) {
                throw new IllegalParamsException("ID不能为空！");
            }
            WorkflowNode workflowNode = new WorkflowNode();
            BeanUtils.copyProperties(param, workflowNode);
            workflowNodeList.add(workflowNode);
        }
        return this.updateBatchById(workflowNodeList);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        WorkflowNode workflowNode1=baseMapper.selectById(idList.get(Constants.DB_FAIL));
        Long workflowId=workflowNode1.getWorkflowId();
        Boolean del=this.removeByIds(idList);
        List<WorkflowNode> workflowNodeList=this.lambdaQuery().eq(WorkflowNode::getWorkflowId,workflowId).orderByAsc(WorkflowNode::getSort).list();
        if (workflowNodeList!=null && !workflowNodeList.isEmpty()){
            for (int i=1;i<workflowNodeList.size()+1;i++) {
                WorkflowNode workflowNode=workflowNodeList.get(i-1);
                workflowNode.setSort(i);
            }
        }
        this.updateBatchById(workflowNodeList);
        return del;
    }

    @Override
    @Transactional(rollbackFor =Exception.class )
    public boolean addBatch(WorkflowNodeAddBatchRequest params) {
        Long count = baseMapper.selectCount(Wrappers.<WorkflowNode>lambdaQuery().eq(WorkflowNode::getWorkflowId,params.getWorkflowId()));
        if(count>Constants.DB_FAIL){
            List<WorkflowNode> workflowNodeList1=this.lambdaQuery().eq(WorkflowNode::getWorkflowId,params.getWorkflowId()).list();
            List<Long> ids=new ArrayList<>();
            for (WorkflowNode workflowNode:workflowNodeList1) {
                ids.add(workflowNode.getId());
            }
            this.removeByIds(ids);
        }
        List<WorkflowNode> workflowNodeList2=new ArrayList<>();
        if(params.getWorkflowNodeUpdateRequestList()!=null && !params.getWorkflowNodeUpdateRequestList().isEmpty()){
            List<String> nodeCodeList=new ArrayList<>();
            for (WorkflowNodeUpdateRequest param:params.getWorkflowNodeUpdateRequestList()) {
                nodeCodeList.add(param.getNodeCode());
                WorkflowNode workflowNode = new WorkflowNode();
                BeanUtils.copyProperties(param, workflowNode);
                workflowNode.setWorkflowId(params.getWorkflowId());
                workflowNode.setNodeCode(param.getSort().toString());
                workflowNodeList2.add(workflowNode);
            }
        }
        return this.saveBatch(workflowNodeList2);
    }

}

