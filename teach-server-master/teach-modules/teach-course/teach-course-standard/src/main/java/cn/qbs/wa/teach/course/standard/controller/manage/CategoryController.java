package cn.qbs.wa.teach.course.standard.controller.manage;

import cn.qbs.wa.teach.basic.api.pojo.DTO.dict.DictResultDTO;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.course.common.entity.Category;
import cn.qbs.wa.teach.course.standard.pojo.category.*;
import cn.qbs.wa.teach.course.standard.service.CategoryService;
import cn.qbs.wa.teach.course.standard.service.CourseCategoryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;


/**
 * 【课程分类】(Category)表控制层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:36
 */
@Api(tags = "课程分类")
@RestController
@RequestMapping("/category")
public class CategoryController {

    /**
     * 服务对象
     */
    @Resource
    private CategoryService categoryService;

    @Resource
    private CourseCategoryService courseCategoryService;


    /**
     * 新增【课程分类】
     *
     */
    @ApiOperation(value = "新增")
    @PostMapping("/add")
    //@RequiresPermissions("category:add")
    @Log(title = "新增【课程分类】", businessType = BusinessType.INSERT)
    public R<Category> add(@RequestBody @Validated CategoryAddRequest params) {
        return R.ok(this.categoryService.add(params));
    }

    @ApiOperation(value = "初始化")
    @PostMapping("/init")
    @Log(title = "初始化【课程分类】", businessType = BusinessType.INSERT)
    public R<Category> init(@RequestBody CategoryAddRequest params) {
        return R.ok(this.categoryService.init(params));
    }

    /**
     * 分页查询【课程分类】
     *
     */
    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    //@RequiresPermissions("category:page")
    @Log(title = "分页查询【课程分类】", businessType = BusinessType.OTHER)
    public R<IPage<CategoryPageResponse>> page(@RequestBody CategoryPageRequest params) {
        return R.ok(this.categoryService.page(params));
    }

    /**
     *【课程分类】列表
     *
     */
    @ApiOperation(value = "列表查询")
    @PostMapping("/list")
    //@RequiresPermissions("category:page")
    @Log(title = "分页查询【课程分类】", businessType = BusinessType.OTHER)
    public R<List<CategoryListResponse>> list(@RequestBody CategoryListRequest params) {
        return R.ok(this.categoryService.list(params));
    }

    /**
     *【课程分类】列表
     *
     */
    @ApiOperation(value = "树形列表查询")
    @PostMapping("/tree")
    //@RequiresPermissions("category:page")
    @Log(title = "分页查询【课程分类】", businessType = BusinessType.OTHER)
    public R<List<CategoryTreeResponse>> tree(@RequestBody CategoryTreeRequest params) {
        if (params.getOrgId() != null) {
            SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
        }
        return R.ok(this.categoryService.tree(params));
    }

    /**
     *【课程分类】列表
     *
     */
    @ApiOperation(value = "树形列表查询,包括已禁用的分类")
    @PostMapping("/treeV2")
    //@RequiresPermissions("category:page")
    @Log(title = "分页查询【课程分类】", businessType = BusinessType.OTHER)
    public R<List<CategoryTreeResponse>> treeV2(@RequestBody CategoryTreeRequest params) {
        if (params.getOrgId() != null) {
            SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
        }
        return R.ok(this.categoryService.treeV2(params));
    }

    /**
     * 查询【课程分类】详情
     *
     * @param request 主键
     */
    @ApiOperation(value = "分类详情")
    @PostMapping("/detail")
    //@RequiresPermissions("category:details")
    @Log(title = "【课程分类】详情", businessType = BusinessType.OTHER)
    public R<CategoryDetailResponse> detail(@RequestBody @Validated IdRequest request) {
        return R.ok(this.categoryService.detail(request.getId()));
    }

    /**
     * 修改【课程分类】
     *
     */
    @ApiOperation(value = "修改分类信息")
    @PostMapping("/update")
    //@RequiresPermissions("category:update")
    @Log(title = "更新【课程分类】", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated CategoryUpdateRequest params) {
        return R.ok(this.categoryService.update(params));
    }

    /**
     * 启/禁用【课程分类】
     *
     */
    @ApiOperation(value = "启/禁用分类")
    @PostMapping("/enable")
    //@RequiresPermissions("category:update")
    @Log(title = "启/禁用【课程分类】", businessType = BusinessType.UPDATE)
    public R<Boolean> enable(@RequestBody @Validated CategoryEnableRequest params) {
        return R.ok(this.categoryService.enable(params.getIdList(), params.getEnabled()));
    }

    /**
     * 删除【课程分类】
     *
     */
    @ApiOperation(value = "删除分类", notes = "支持批量删除")
    @PostMapping("/delete")
    //@RequiresPermissions("category:delete")
    @Log(title = "删除【课程分类】", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody @Validated IdListRequest request) {
        List<Long> idList = request.getIdList();

        List<Category> categories = this.categoryService.listByIds(idList);
        if (categories.isEmpty()) {
            return R.fail("分类已被删除");
        }
        // 2022-06-07 原有的引用的通用分类允许删除
        // 通用分类只能禁用不允许删除
        //List<Long> templateIds = categories.stream().filter(c -> Integer.valueOf(1).equals(c.getTemplate())).map(Category::getId).collect(Collectors.toList());
        //if (!templateIds.isEmpty()) {
        //    return R.fail(this.validMsg(templateIds, Collectors.joining(",", "[", "] 为通用分类，只能禁用不允许删除")));
        //}

        // 查询一下是否存在不允许删除的分类
        // 1.分类下包含子分类不允许删除
        List<Long> illegalIdList = idList.stream().filter(id -> this.categoryService.hasChildren(id)).collect(Collectors.toList());
        if (!illegalIdList.isEmpty()) {
            return R.fail(this.validMsg(illegalIdList, Collectors.joining(",", "[", "] 分类下，存在子分类无法删除，请先删除子分类")));
        }
        // 2.分类下存在课程不允许删除
        illegalIdList = idList.stream().filter(id -> this.courseCategoryService.hasCourse(id)).collect(Collectors.toList());
        if (!illegalIdList.isEmpty()) {
            return R.fail(this.validMsg(illegalIdList, Collectors.joining(",", "[", "] 分类下，存在关联课程无法删除")));
        }
        return R.ok(this.categoryService.deleteByIds(idList));
    }

    private String validMsg(List<Long> idList, Collector<CharSequence, ?, String> join) {
        List<Category> categories = this.categoryService.lambdaQuery().select(Category::getCategoryName).in(Category::getId, idList).list();
        if (!categories.isEmpty()) {
            return categories.stream().map(Category::getCategoryName).collect(join);
        }
        return "分类已被删除";
    }
}

