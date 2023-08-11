package cn.qbs.wa.train.allowance.service;

import cn.qbs.wa.train.allowance.entity.WorkflowNode;
import cn.qbs.wa.train.allowance.pojo.workflownode.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 流程节点(WorkflowNode)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-13 13:59:24
 */
public interface WorkflowNodeService extends IService<WorkflowNode> {

    /**
     * 新增流程节点
     * @param params
     * @return
     */
    boolean add(List<WorkflowNodeAddRequest> params);

    /**
     * 分页查询流程节点
     * @param params
     * @return
     */
    IPage<WorkflowNodePageResponse> page(WorkflowNodePageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    WorkflowNodeDetailResponse detail(Serializable id);

    /**
     * 更新流程节点
     * @param params
     * @return
     */
    boolean update(List<WorkflowNodeUpdateRequest> params);

    /**
     * 删除流程节点
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    boolean addBatch(WorkflowNodeAddBatchRequest params);
    
}

