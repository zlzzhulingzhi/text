package cn.qbs.wa.teach.course.standard.controller.platform;

import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.PlatformIdListRequest;
import cn.qbs.wa.teach.common.core.domain.PlatformIdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.course.common.entity.Category;
import cn.qbs.wa.teach.course.common.entity.CourseCategory;
import cn.qbs.wa.teach.course.standard.pojo.category.*;
import cn.qbs.wa.teach.course.standard.service.CategoryService;
import cn.qbs.wa.teach.course.standard.service.CourseCategoryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 【课程分类】(Category)表控制层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-23 09:42:32
 */
@ApiIgnore
@Api(tags = "课程分类(平台运维人员调用接口)", hidden = true)
//@RestController
//@RequestMapping("/platform/category")
public class PlatformCategoryController {

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
        if (params.getOrgId() == null) {
            return R.fail("请选择机构");
        }
        SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
        return R.ok(this.categoryService.add(params));
    }

    /**
     * 分页查询【课程分类】
     *
     */
    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    //@RequiresPermissions("category:page")
    @Log(title = "分页查询【课程分类】", businessType = BusinessType.OTHER)
    public R<IPage<CategoryPageResponse>> page(@RequestBody @Validated CategoryPageRequest params) {
        SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
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
        if (params.getOrgId() == null) {
            return R.fail("请选择机构");
        }
        SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
        return R.ok(this.categoryService.list(params));
    }

    /**
     * 查询【课程分类】详情
     *
     * @param params 主键
     */
    @ApiOperation(value = "分类详情")
    @PostMapping("/detail")
    //@RequiresPermissions("category:details")
    @Log(title = "【课程分类】详情", businessType = BusinessType.OTHER)
    public R<CategoryDetailResponse> detail(@RequestBody @Validated PlatformIdRequest params) {
        SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
        return R.ok(this.categoryService.detail(params.getId()));
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
        SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
        return R.ok(this.categoryService.update(params));
    }

    /**
     * 删除【课程分类】
     *
     */
    @ApiOperation(value = "删除分类信息", notes = "支持批量删除")
    @PostMapping("/delete")
    //@RequiresPermissions("category:delete")
    @Log(title = "删除【课程分类】", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody @Validated PlatformIdListRequest params) {
        SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
        List<Long> idList = params.getIdList();
        // 查询一下是否存在不允许删除的分类
        List<Long> illegalIdList = idList.stream().filter(id -> {
            long count = courseCategoryService.count(Wrappers.<CourseCategory>lambdaQuery().eq(CourseCategory::getCategoryId, id));
            return count > 0;
        }).collect(Collectors.toList());

        if (!illegalIdList.isEmpty()) {
            List<Category> categories = categoryService.lambdaQuery().select(Category::getCategoryName).in(Category::getId, illegalIdList).list();
            if (!categories.isEmpty()) {
                return R.fail(categories.stream().map(Category::getCategoryName).collect(Collectors.joining(",", "[", "] 分类下，存在关联课程无法删除")));
            }
        }
        return R.ok(this.categoryService.deleteByIds(idList));
    }

}

