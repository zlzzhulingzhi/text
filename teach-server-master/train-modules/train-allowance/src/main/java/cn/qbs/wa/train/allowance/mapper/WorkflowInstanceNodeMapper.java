package cn.qbs.wa.train.allowance.mapper;

import cn.qbs.wa.train.allowance.entity.WorkflowInstanceNode;
import cn.qbs.wa.train.allowance.pojo.approve.ApprovePageRequest;
import cn.qbs.wa.train.allowance.pojo.workflownode.WorkflowInstanceNodeResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 流程节点实例(WorkflowInstanceNode)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-13 13:56:09
 */
public interface WorkflowInstanceNodeMapper extends BaseMapper<WorkflowInstanceNode> {


    List<WorkflowInstanceNode> selectByParams(@Param("params") ApprovePageRequest approvePageRequest);

    List<WorkflowInstanceNodeResponse> selectByApplyId(Serializable id);

    WorkflowInstanceNodeResponse selectByApplyIdAndNode(Serializable id);
}

