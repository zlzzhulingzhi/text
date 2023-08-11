package cn.qbs.wa.teach.question.controller;


import cn.qbs.wa.teach.common.core.domain.*;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.question.pojo.SelectOption;
import cn.qbs.wa.teach.question.pojo.category.*;
import cn.qbs.wa.teach.question.service.CategoryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * 试题分类(QuestionCategory)表控制层
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 18:02:43
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
        return R.ok(this.categoryService.enable(request.getIdList(), request.getEnabled()));
    }

    /**
     * 获取前端下拉选择数据
     * 此接口已废弃，请调用 tree 接口
     *
     * @return 所有数据
     */
    @Deprecated
    @PostMapping("selectOptionList")
    //@RequiresPermissions("category:selectOptionList")
    //@Log(title = "获取前端下拉选择分类数据", businessType = BusinessType.OTHER)
    public R<List<SelectOption>> selectOptionList() {
        List<SelectOption> selectList = this.categoryService.getSelectOptionList();
        return R.ok(selectList);
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
     * 获取分类下的试题数量
     *
     * @return
     */
    @PostMapping("/question/count")
    @ApiOperation("获取分类下的试题数量")
    //@RequiresPermissions("question:category:count")
    @Log(title = "获取分类下的试题数量", businessType = BusinessType.QUERY)
    public R<Long> categoryQuestionCount(@RequestBody @Validated CategoryQuestionCountRequest param) {
        return R.ok(this.categoryService.getCategoryQuestionCount(param));
    }

    /**
     * 获取分类下的试卷数量
     *
     * @return
     */
    @PostMapping("/paper/count")
    @ApiOperation("获取分类下的试卷数量")
    //@RequiresPermissions("paper:category:count")
    @Log(title = "获取分类下的试卷数量", businessType = BusinessType.QUERY)
    public R<Long> categoryPaperCount(@RequestBody @Validated IdListParam param) {
        return R.ok(this.categoryService.getCategoryPaperCount(param.getIdList()));
    }

}

