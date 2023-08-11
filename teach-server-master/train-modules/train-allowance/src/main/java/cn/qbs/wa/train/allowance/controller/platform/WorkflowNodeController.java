package cn.qbs.wa.train.allowance.controller.platform;


import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.train.allowance.pojo.workflownode.*;
import cn.qbs.wa.train.allowance.service.WorkflowNodeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * 流程节点(WorkflowNode)表控制层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-13 13:59:24
 */
@RestController
@RequestMapping("/workflowNode")
@Api(tags = "流程节点维护")
public class WorkflowNodeController {

    /**
     * 服务对象
     */
    @Resource
    private WorkflowNodeService workflowNodeService;

    /**
     * 新增流程节点
     *
     * @param params
     * @return 
     */
    @PostMapping("/add")
    @ApiOperation("新增流程节点")
    //@RequiresPermissions("workflowNode:add")
    public R<Boolean> add(@RequestBody @Validated List<WorkflowNodeAddRequest> params) {
        return R.ok(this.workflowNodeService.add(params));
    }
    
    /**
     * 分页查询流程节点
     *
     * @param params
     * @return 
     */
    @PostMapping("/page")
    @ApiOperation("查询流程节点")
    @RequiresPermissions("System:Flow")
    public R<IPage<WorkflowNodePageResponse>> page(@RequestBody WorkflowNodePageRequest params) {
        return R.ok(this.workflowNodeService.page(params));
    }

    /**
     * 查询流程节点详情
     *
     * @param id 主键
     * @return 
     */
    @PostMapping("/detail")
    @ApiOperation("流程节点详情")
    //@RequiresPermissions("workflowNode:details")
    public R<WorkflowNodeDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.workflowNodeService.detail(request.getId()));
    }

    /**
     * 修改流程节点
     *
     * @param params 
     * @return 
     */
    @PostMapping("/update")
    @ApiOperation("修改流程节点")
    //@RequiresPermissions("workflowNode:update")
    public R<Boolean> update(@RequestBody @Validated List<WorkflowNodeUpdateRequest> params) {
        return R.ok(this.workflowNodeService.update(params));
    }

    /**
     * 删除流程节点
     *
     * @param idList 主键集合
     * @return 
     */
    @PostMapping("/delete")
    @ApiOperation("删除流程节点")
    //@RequiresPermissions("workflowNode:delete")
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.workflowNodeService.deleteByIds(request.getIdList()));
    }

    @PostMapping("/addBatch")
    @ApiOperation("批量新增流程节点")
    @RequiresPermissions("System:Flow")
    public R<Boolean> addBatch(@RequestBody @Validated WorkflowNodeAddBatchRequest params) {
        return R.ok(this.workflowNodeService.addBatch(params));
    }
    
}

