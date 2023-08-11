package cn.qbs.wa.train.allowance.mapper;

import cn.qbs.wa.train.allowance.entity.Workflow;
import cn.qbs.wa.train.allowance.pojo.workflow.WorkflowDetailResponse;
import cn.qbs.wa.train.allowance.pojo.workflow.WorkflowPageRequest;
import cn.qbs.wa.train.allowance.pojo.workflow.WorkflowPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;

/**
 * 流程定义(Workflow)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-13 13:58:46
 */
public interface WorkflowMapper extends BaseMapper<Workflow> {

    IPage<WorkflowPageResponse> page(@Param("page") IPage<?> page, @Param("params") WorkflowPageRequest params);

    WorkflowDetailResponse selectDetailById(Serializable id);
    
}

