package cn.qbs.wa.teach.course.standard.controller.platform;


import cn.qbs.wa.teach.common.core.domain.R;

import cn.qbs.wa.teach.course.standard.pojo.tactivity.*;
import cn.qbs.wa.teach.course.standard.service.TActivityService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;


/**
 * 活动表(TActivity)表控制层
 *
 * @author makejava
 * @since 2022-12-13 15:55:03
 */
@RestController
@RequestMapping("tActivity")
@Api(tags = "活动")
public class TActivityController {

    /**
     * 服务对象
     */
    @Resource
    private TActivityService tActivityService;


    /**
     * 新增活动表
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    @ApiOperation(value = "新增活动")
    //@RequiresPermissions("tActivity:add")
    //@Log(title = "新增活动表", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated TActivityAddRequest params) {
        return R.ok(this.tActivityService.add(params));
    }

    /**
     * 分页查询活动表
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    @ApiOperation(value = "分页查询活动")
    //@RequiresPermissions("tActivity:page")
    //@Log(title = "分页查询活动表", businessType = BusinessType.OTHER)
    public R<IPage<TActivityPageResponse>> page(@RequestBody TActivityPageRequest params) {
        return R.ok(this.tActivityService.page(params));
    }

    /**
     * 查询活动表详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    @ApiOperation(value = "活动详情")
    //@RequiresPermissions("tActivity:details")
    //@Log(title = "活动表详情", businessType = BusinessType.OTHER)
    public R<TActivityDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.tActivityService.detail(request.getId()));
    }

    /**
     * 修改活动表
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    @ApiOperation(value = "修改活动")
    //@RequiresPermissions("tActivity:update")
    //@Log(title = "更新活动表", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated TActivityUpdateRequest params) {
        return R.ok(this.tActivityService.update(params));
    }

    /**
     * 删除活动表
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    @ApiOperation(value = "删除活动")
    //@RequiresPermissions("tActivity:delete")
    //@Log(title = "删除活动表", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.tActivityService.deleteByIds(request.getIdList()));
    }

    @PostMapping("updateShelfStatus")
    @ApiOperation(value = "上下架")
    public R<Boolean> updateShelfStatus(@RequestBody @Validated TActivityUpdateShelfStatusRequest params) {
        return R.ok(this.tActivityService.updateShelfStatus(params));
    }

}

