package cn.qbs.wa.train.allowance.controller.platform;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.train.allowance.pojo.workflow.*;
import cn.qbs.wa.train.allowance.service.WorkflowService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 流程定义(Workflow)表控制层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-13 13:58:46
 */
@RestController
@RequestMapping("/workflow")
@Api(tags = "流程定义维护")
public class WorkflowController {

    /**
     * 服务对象
     */
    @Resource
    private WorkflowService workflowService;

    /**
     * 新增流程定义
     *
     * @param params
     * @return 
     */
    @PostMapping("/add")
    @ApiOperation("新增流程定义")
    @RequiresPermissions("System:Flow")
    public R<Boolean> add(@RequestBody @Validated WorkflowAddRequest params) {
        return R.ok(this.workflowService.add(params));
    }
    
    /**
     * 分页查询流程定义
     *
     * @param params
     * @return 
     */
    @PostMapping("/page")
    @ApiOperation("查询流程定义")
    @RequiresPermissions("System:Flow")
    public R<IPage<WorkflowPageResponse>> page(@RequestBody WorkflowPageRequest params) {
        return R.ok(this.workflowService.page(params));
    }

    /**
     * 查询流程定义详情
     *
     * @param id 主键
     * @return 
     */
    @PostMapping("/detail")
    @ApiOperation("流程定义详情")
    @RequiresPermissions("System:Flow")
    public R<WorkflowDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.workflowService.detail(request.getId()));
    }

    /**
     * 修改流程定义
     *
     * @param params 
     * @return 
     */
    @PostMapping("/update")
    @ApiOperation("修改流程定义")
    @RequiresPermissions("System:Flow")
    public R<Boolean> update(@RequestBody @Validated WorkflowUpdateRequest params) {
        return R.ok(this.workflowService.update(params));
    }

    /**
     * 删除流程定义
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation("删除流程定义")
    @RequiresPermissions("System:Flow")
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.workflowService.deleteByIds(request.getIdList()));
    }

    @PostMapping("updateBatch")
    @ApiOperation("批量启用禁用")
    public R<Boolean> updateBatch(@RequestBody @Validated WorkflowUpdateBatchRequest params) {
        return R.ok(this.workflowService.updateBatch(params));
    }

    
}

