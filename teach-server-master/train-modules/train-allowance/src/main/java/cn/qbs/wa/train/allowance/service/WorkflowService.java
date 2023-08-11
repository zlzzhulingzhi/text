package cn.qbs.wa.train.allowance.service;

import cn.qbs.wa.train.allowance.entity.Workflow;
import cn.qbs.wa.train.allowance.pojo.workflow.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 流程定义(Workflow)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-13 13:58:46
 */
public interface WorkflowService extends IService<Workflow> {

    /**
     * 新增流程定义
     * @param params
     * @return
     */
    boolean add(WorkflowAddRequest params);

    /**
     * 分页查询流程定义
     * @param params
     * @return
     */
    IPage<WorkflowPageResponse> page(WorkflowPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    WorkflowDetailResponse detail(Serializable id);

    /**
     * 更新流程定义
     * @param params
     * @return
     */
    boolean update(WorkflowUpdateRequest params);

    /**
     * 删除流程定义
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    boolean updateBatch(WorkflowUpdateBatchRequest params);
    
}

