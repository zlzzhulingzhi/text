package cn.qbs.wa.teach.exam.admin.controller;


import cn.qbs.wa.teach.common.core.domain.EnableRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.ParentIdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.exam.admin.pojo.category.*;
import cn.qbs.wa.teach.exam.admin.service.CategoryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;



/**
 * 分类(Category)表控制层
 *
 * @author zcm
 * @since 2021-12-14 13:47:16
 */
@RestController
@RequestMapping("category")
public class CategoryController {

    /**
     * 服务对象
     */
    @Resource
    private CategoryService categoryService;


    /**
     * 新增分类
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("category:add")
    //@Log(title = "新增分类", businessType = BusinessType.INSERT)
    public R<Long> add(@RequestBody @Validated CategoryAddRequest params) {
        return R.ok(this.categoryService.add(params));
    }

    /**
     * 分页查询分类
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("category:page")
    //@Log(title = "分页查询分类", businessType = BusinessType.OTHER)
    public R<IPage<CategoryPageResponse>> page(@RequestBody CategoryPageRequest params) {
        return R.ok(this.categoryService.page(params));
    }

    /**
     * 查询分类详情
     *
     * @param request
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("category:details")
    //@Log(title = "分类详情", businessType = BusinessType.OTHER)
    public R<CategoryDetailResponse> detail(@RequestBody @Validated IdRequest request) {
        return R.ok(this.categoryService.detail(request.getId()));
    }

    /**
     * 修改分类
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("category:update")
    //@Log(title = "更新分类", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated CategoryUpdateRequest params) {
        return R.ok(this.categoryService.update(params));
    }

    /**
     * 启用停用分类
     *
     * @param request
     * @return
     */
    @PostMapping("enable")
    //@RequiresPermissions("category:enable")
    //@Log(title = "启用停用分类", businessType = BusinessType.UPDATE)
    public R<Boolean> enable(@RequestBody @Validated EnableRequest request) {
        return R.ok(this.categoryService.enable(request));
    }

    /**
     * 获取子分类
     *
     * @return
     */
    @PostMapping("children/list")
    //@RequiresPermissions("category:children")
    //@Log(title = "获取树形结构", businessType = BusinessType.OTHER)
    public R<List<CategoryPageResponse>> childrenList(@RequestBody @Validated ParentIdRequest request) {
        return R.ok(this.categoryService.childrenList(request.getParentId()));
    }

    /**
     * 删除分类
     *
     * @param request
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("category:delete")
    //@Log(title = "删除分类", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody @Validated IdRequest request) {
        return R.ok(this.categoryService.deleteById(request.getId()));
    }

    /**
     * 获取树形分类列表
     *
     * @return
     */
    @PostMapping("/treeList")
    //@RequiresPermissions("paper:tree")
    //@Log(title = "获取树形分类列表", businessType = BusinessType.OTHER)
    public R<List> treeList() {
        return R.ok(this.categoryService.getTreeList());
    }

}
