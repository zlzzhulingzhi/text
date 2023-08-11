package cn.qbs.wa.train.logistics.controller.platform;


import cn.qbs.wa.teach.common.core.domain.R;

import cn.qbs.wa.train.logistics.entity.Unit;
import cn.qbs.wa.train.logistics.pojo.unit.*;
import cn.qbs.wa.train.logistics.service.platform.UnitService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;

import java.util.List;


/**
 * 单位表(Unit)表控制层
 *
 * @author makejava
 * @since 2022-09-29 08:31:21
 */
@RestController
@RequestMapping("unit")
@Api(tags = "单位管理")
public class UnitController {

    /**
     * 服务对象
     */
    @Resource
    private UnitService unitService;


    /**
     * 新增单位表
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    @ApiOperation("新增单位")
    //@RequiresPermissions("unit:add")
    //@Log(title = "新增单位表", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated UnitAddRequest params) {
        return R.ok(this.unitService.add(params));
    }

    /**
     * 分页查询单位表
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    @ApiOperation("分页查询单位")
    //@RequiresPermissions("unit:page")
    //@Log(title = "分页查询单位表", businessType = BusinessType.OTHER)
    public R<IPage<UnitPageResponse>> page(@RequestBody UnitPageRequest params) {
        return R.ok(this.unitService.page(params));
    }
    /**
     * 查询单位列表
     */
    @PostMapping("list")
    @ApiOperation("查询单位列表")
    public R<List<Unit>> listUnit() {
        return R.ok(this.unitService.listUnit());
    }

    /**
     * 查询单位表详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    @ApiOperation("单位详情")
    //@RequiresPermissions("unit:details")
    //@Log(title = "单位表详情", businessType = BusinessType.OTHER)
    public R<UnitDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.unitService.detail(request.getId()));
    }

    /**
     * 修改单位表
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    @ApiOperation("更新单位")
    //@RequiresPermissions("unit:update")
    //@Log(title = "更新单位表", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated UnitUpdateRequest params) {
        return R.ok(this.unitService.update(params));
    }

    /**
     * 批量修改单位表
     *
     * @param params
     * @return
     */
    @PostMapping("updateBatch")
    @ApiOperation("批量更新单位")
    public R<Boolean> updateBatch(@RequestBody @Validated List<UnitUpdateRequest> params) {
        return R.ok(this.unitService.updateBatch(params));
    }

    /**
     * 删除单位表
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    @ApiOperation("删除单位")
    //@RequiresPermissions("unit:delete")
    //@Log(title = "删除单位表", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.unitService.deleteByIds(request.getIdList()));
    }

}

