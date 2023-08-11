package cn.qbs.wa.teach.question.controller;


import cn.qbs.wa.teach.common.core.domain.EnableRequest;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.teach.question.enumerate.CategoryGroupEnum;
import cn.qbs.wa.teach.question.pojo.category.CategoryTreeNode;
import cn.qbs.wa.teach.question.pojo.question.*;
import cn.qbs.wa.teach.question.service.CategoryService;
import cn.qbs.wa.teach.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 试题(Question)表控制层
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 18:17:25
 */
@RestController
@RequestMapping("question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    private final CategoryService categoryService;


    /**
     * 新增试题
     *
     * @param params
     * @return 
     */
    @PostMapping("add")
    //@RequiresPermissions("question:add")
    //@Log(title = "新增试题", businessType = BusinessType.INSERT)
    public R<Long> add(@RequestBody @Validated QuestionAddRequest params) {
        return R.ok(this.questionService.add(params));
    }

    /**
     * 查询试题详情
     *
     * @param request
     * @return 
     */
    @PostMapping("detail")
    @RequiresPermissions("System:Education:Question:List")
    //@Log(title = "试题详情", businessType = BusinessType.OTHER)
    public R<QuestionDetailResponse> detail(@RequestBody @Validated IdRequest request) {
        return R.ok(this.questionService.detail(request.getId()));
    }

    /**
     * 修改试题
     *
     * @param params 
     * @return 
     */
    @PostMapping("update")
    //@RequiresPermissions("question:update")
    //@Log(title = "更新试题", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated QuestionUpdateRequest params) {
        return R.ok(this.questionService.update(params));
    }

    /**
     * 启用停用试题
     *
     * @param request
     * @return
     */
    @PostMapping("enable")
    //@RequiresPermissions("question:enable")
    //@Log(title = "启用停用试题", businessType = BusinessType.UPDATE)
    public R<Boolean> enable(@RequestBody @Validated EnableRequest request) {
        return R.ok(this.questionService.enable(request));
    }

    /**
     * 删除试题
     *
     * @param request
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("question:delete")
    //@Log(title = "删除试题", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody @Validated IdListRequest request) {
        this.questionService.deleteByIds(request.getIdList());
        return R.ok(true);
    }

    /**
     * Excel试题解析
     * @return
     */
    @Deprecated
    @PostMapping("/excelParse")
    //@RequiresPermissions("question:excelParse")
    //@Log(title = "Excel试题解析", businessType = BusinessType.IMPORT)
    public R<List<ExcelQuestionParseResult>> excelParse(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        return R.ok(questionService.excelParse(multipartFile));
    }

    /**
     * Excel试题批量保存
     * @return
     */
    @Deprecated
    @PostMapping("/excelQuestionBatchSave")
//    @RequiresPermissions("question:excelQuestionBatchSave")
    //@Log(title = "Excel试题批量保存", businessType = BusinessType.IMPORT)
    public R<List<ExcelQuestionSaveResult>> excelQuestionBatchSave(@RequestBody List<ExcelQuestionSaveTo> excelQuestionList) throws Exception {
        return questionService.excelQuestionBatchSave(excelQuestionList);
    }

    /**
     * 获取试题分类
     *
     * @return
     */
    @PostMapping("/category")
    //@RequiresPermissions("question:category")
    //@Log(title = "获取试题分类", businessType = BusinessType.OTHER)
    public R<List<CategoryTreeNode>> category() {
        return R.ok(this.categoryService.getTreeList(CategoryGroupEnum.QUESTION.getId(), true));
    }

    /**
     * 获取所有试题分类
     *
     * @return
     */
    @PostMapping("/category/all")
    //@RequiresPermissions("question:category:all")
    //@Log(title = "获取所有试题分类", businessType = BusinessType.OTHER)
    public R<List<CategoryTreeNode>> allCategory() {
        return R.ok(this.categoryService.getTreeList(CategoryGroupEnum.QUESTION.getId(), null));
    }

    /**
     * 好像没有地方调用
     * 获取试题详情
     * @param request
     * @return
     */
//    @PostMapping("/details")
    public R<List<QuestionDetailResponse>> details(@RequestBody @Validated IdListRequest request) {
        return R.ok(this.questionService.details(request.getIdList()));
    }

}

