package cn.qbs.wa.train.logistics.controller;/*
package cn.qbs.wa.train.organization.controller;


import cn.qbs.wa.teach.common.core.domain.BatchFlagRequest;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.train.organization.pojo.category.*;
import cn.qbs.wa.train.organization.service.CategoryService;
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


*/
/**
 * 通用分类(Category)表控制层
 *
 * @author makejava
 * @since 2022-01-18 09:48:41
 *//*

@RestController
@RequestMapping("category")
@Api(tags = "通用分类管理")
public class CategoryController {

    */
/**
     * 服务对象
     *//*

    @Resource
    private CategoryService categoryService;


    */
/**
     * 新增通用分类
     *
     * @param params
     * @return
     *//*

    @PostMapping("add")
    //@RequiresPermissions("category:add")
    //@Log(title = "新增通用分类", businessType = BusinessType.INSERT)
    @ApiOperation("新增")
    public R<Boolean> add(@RequestBody @Validated CategoryAddRequest params) {
        return R.ok(this.categoryService.add(params));
    }

    @PostMapping("children/list")
    //@RequiresPermissions("dict:page")
    @Log(title = "通用分类子列表", businessType = BusinessType.OTHER)
    @ApiOperation("通用分类子列表")
    public R<List<CategoryListResponse>> childList(@RequestBody CategoryListRequest params) {
        return R.ok(this.categoryService.childList(params));
    }


    @PostMapping("tree/list")
    //@RequiresPermissions("category:page")
    @ApiOperation(value = "树形列表查询")
    public R<List<CategoryTreeResponse>> tree(@RequestBody CategoryTreeRequest params) {
        return R.ok(this.categoryService.tree(params));
    }

    */
/**
     * 分页查询通用分类
     *
     * @param params
     * @return
     *//*

    @PostMapping("page")
    //@RequiresPermissions("category:page")
    //@Log(title = "分页查询通用分类", businessType = BusinessType.OTHER)
    @ApiOperation("分页查询通用分类")
    public R<IPage<CategoryPageResponse>> page(@RequestBody CategoryPageRequest params) {
        return R.ok(this.categoryService.page(params));
    }

    */
/**
     * 查询通用分类详情
     *
     * @param id 主键
     * @return
     *//*

    @PostMapping("detail")
    //@RequiresPermissions("category:details")
    //@Log(title = "通用分类详情", businessType = BusinessType.OTHER)
    @ApiOperation("详情")
    public R<CategoryDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.categoryService.detail(request.getId()));
    }

    */
/**
     * 修改通用分类
     *
     * @param params
     * @return
     *//*

    @PostMapping("update")
    //@RequiresPermissions("category:update")
    //@Log(title = "更新通用分类", businessType = BusinessType.UPDATE)
    @ApiOperation("更新")
    public R<Boolean> update(@RequestBody @Validated CategoryUpdateRequest params) {
        return R.ok(this.categoryService.update(params));
    }

    */
/**
     * 删除通用分类
     *
     * @param idList 主键集合
     * @return
     *//*

    @PostMapping("delete")
    //@RequiresPermissions("category:delete")
    //@Log(title = "删除通用分类", businessType = BusinessType.DELETE)
    @ApiOperation("删除")
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.categoryService.deleteByIds(request.getIdList()));
    }

    */
/**
     * 批量启用/禁用
     *//*

    @PostMapping("batch-enabled")
    //@RequiresPermissions("role:delete")
    @Log(title = "批量启用/禁用】", businessType = BusinessType.OTHER)
    @ApiOperation("批量启用/禁用")
    public R batchEnabled(@RequestBody BatchFlagRequest request) {
        this.categoryService.batchEnabled(request.getFlag(), request.getIdList());
        return R.ok();
    }

}

*/
