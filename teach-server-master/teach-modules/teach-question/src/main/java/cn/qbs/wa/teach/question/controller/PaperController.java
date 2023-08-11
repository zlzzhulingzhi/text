package cn.qbs.wa.teach.question.controller;


import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.EnableRequest;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdOrgRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.question.elasticsearch.SearchPageResult;
import cn.qbs.wa.teach.question.entity.Paper;
import cn.qbs.wa.teach.question.enumerate.CategoryGroupEnum;
import cn.qbs.wa.teach.question.pojo.category.CategoryTreeNode;
import cn.qbs.wa.teach.question.pojo.paper.*;
import cn.qbs.wa.teach.question.service.CategoryService;
import cn.qbs.wa.teach.question.service.PaperService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 试卷(Paper)表控制层
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-18 20:48:48
 */
@RestController
@RequestMapping("paper")
@RequiredArgsConstructor
public class PaperController {

    private final PaperService paperService;

    private final CategoryService categoryService;

    /**
     * 新增试卷
     *
     * @param params
     * @return 
     */
    @PostMapping("add")
    //@RequiresPermissions("paper:add")
    //@Log(title = "新增试卷", businessType = BusinessType.INSERT)
    public R<Long> add(@RequestBody @Validated PaperAddRequest params) {
        return R.ok(this.paperService.add(params));
    }
    
    /**
     * 分页查询试卷
     *
     * @param params
     * @return 
     */
    @PostMapping("page")
    //@RequiresPermissions("paper:page")
    //@Log(title = "分页查询试卷", businessType = BusinessType.OTHER)
    public R<IPage<PaperPageResponse>> page(@RequestBody PaperPageRequest params) {
        return R.ok(this.paperService.page(params));
    }

    /**
     * 查询试卷详情
     *
     * @param request
     * @return 
     */
    @PostMapping("detail")
    //@RequiresPermissions("paper:details")
    //@Log(title = "试卷详情", businessType = BusinessType.OTHER)
    public R<PaperDetailResponse> detail(@RequestBody @Validated IdOrgRequest request) {
        if (request.getOrgId() != null) {
            SecurityContextHolder.setSelectOrgId(String.valueOf(request.getOrgId()));
        }
        return R.ok(this.paperService.detail(request.getId()));
    }

    /**
     * 修改试卷
     *
     * @param params 
     * @return 
     */
    @PostMapping("update")
    //@RequiresPermissions("paper:update")
    //@Log(title = "更新试卷", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated PaperUpdateRequest params) {
        return R.ok(this.paperService.update(params));
    }

    /**
     * 启用停用试卷
     *
     * @param request
     * @return
     */
    @PostMapping("enable")
    //@RequiresPermissions("paper:enable")
    //@Log(title = "启用停用试卷", businessType = BusinessType.UPDATE)
    public R<Boolean> enable(@RequestBody @Validated EnableRequest request) {
        return R.ok(this.paperService.enable(request));
    }


    /**
     * 试卷搜索
     * @param searchRequest
     * @return
     */
    @PostMapping("/search")
    //@RequiresPermissions("paper:search")
    //@Log(title = "试卷搜索", businessType = BusinessType.OTHER)
    public R<SearchPageResult<PaperSearchResult>> search(@RequestBody PaperSearchRequest searchRequest) {
        searchRequest.setOrgId(SecurityContextHolder.getOrgId());
        return R.ok(this.paperService.search(searchRequest));
    }


    /**
     * 获取试卷分类
     *
     * @return
     */
    @PostMapping("/category")
    //@RequiresPermissions("paper:category")
    //@Log(title = "获取试卷分类", businessType = BusinessType.OTHER)
    public R<List> category() {
        return R.ok(this.categoryService.getTreeList(CategoryGroupEnum.PAPER.getId(), true));
    }


    /**
     * 获取所有试卷分类
     *
     * @return
     */
    @PostMapping("/category/all")
    //@RequiresPermissions("paper:category:all")
    //@Log(title = "获取所有试卷分类", businessType = BusinessType.OTHER)
    public R<List<CategoryTreeNode>> allCategory() {
        return R.ok(this.categoryService.getTreeList(CategoryGroupEnum.PAPER.getId(), null));
    }

    /**
     * 修改试卷可编辑状态
     *
     * @param request
     * @return
     */
    @PostMapping("editable")
    //@RequiresPermissions("paper:editable")
    //@Log(title = "修改试卷可编辑状态", businessType = BusinessType.UPDATE)
    public R<Boolean> editable(@RequestBody @Validated UpdatePaperEditableRequest request) {
        return R.ok(this.paperService.upddateEditable(request));
    }

    /**
     * 查询基本试卷列表
     *
     * @param
     * @return
     */
    @PostMapping("basicList")
    //@RequiresPermissions("paper:basicList")
    //@Log(title = "查询基本试卷列表", businessType = BusinessType.OTHER)
    @ApiOperation("查询基本试卷列表")
    public R<List<Paper>> basicList(@RequestBody IdListRequest param) {
        if (param.getOrgId() != null) {
            SecurityContextHolder.setSelectOrgId(String.valueOf(param.getOrgId()));
        }
        List<Paper> paperList = this.paperService.lambdaQuery().in(Paper::getId, param.getIdList()).list();
        return R.ok(paperList);
    }

}

