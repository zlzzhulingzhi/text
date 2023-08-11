package cn.qbs.wa.train.allowance.mapper;

import cn.qbs.wa.train.allowance.entity.WorkflowNode;
import cn.qbs.wa.train.allowance.pojo.workflownode.WorkflowNodeDetailResponse;
import cn.qbs.wa.train.allowance.pojo.workflownode.WorkflowNodePageRequest;
import cn.qbs.wa.train.allowance.pojo.workflownode.WorkflowNodePageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 流程节点(WorkflowNode)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-13 13:59:24
 */
public interface WorkflowNodeMapper extends BaseMapper<WorkflowNode> {

    IPage<WorkflowNodePageResponse> page(@Param("page") IPage<?> page, @Param("params") WorkflowNodePageRequest params);

    WorkflowNodeDetailResponse selectDetailById(Serializable id);

    List<WorkflowNode> listByFlowCode(String flowCode);
}

