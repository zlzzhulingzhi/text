package cn.qbs.wa.teach.question.controller;

import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.question.elasticsearch.SearchPageResult;
import cn.qbs.wa.teach.question.pojo.question.search.*;
import cn.qbs.wa.teach.question.service.QuestionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * 试题搜索 Controller
 * @Author zcm
 * @Date 2021/11/17 09:58
 * @Version 1.0
 */
@RestController
public class QuestionSearchController {

    @Resource
    private QuestionService questionService;

    /**
     * 试题搜索
     * @param searchRequest
     * @return
     */
    @PostMapping("/question/search")
    //@RequiresPermissions("question:search")
    //@Log(title = "分页查询试题", businessType = BusinessType.OTHER)
    public R<SearchPageResult<QuestionSearchResult>> search(@RequestBody QuestionSearchRequest searchRequest) {
        searchRequest.setOrgId(SecurityContextHolder.getOrgId());
        return R.ok(this.questionService.search(searchRequest));
    }

    /**
     * 试题分类计数(已废弃)
     * 废弃原因：
     *      由于父分类的题数要包含子分类的题数，通过ES来实现比较复杂，再通过MySQL查询，数据量大了效率较低。
     *      所以此接口已废弃，新方案只需要显示总题数即可
     * @return
     */
    @Deprecated
    @PostMapping("/question/count/category")
    //@RequiresPermissions("question:countCategory")
    //@Log(title = "试题分类计数", businessType = BusinessType.OTHER)
    public R<List<?>> countCategory() throws IOException {
        return R.ok(this.questionService.countCategory(SecurityContextHolder.getOrgId()));
    }

    /**
     * 试题查询-分类
     * @return 试题分类树形列表，在虚拟根节点包含总题数
     */
    @Deprecated
    @PostMapping("/question/search/category")
    //@RequiresPermissions("question:searchCategory")
    //@Log(title = "试题查询-分类", businessType = BusinessType.OTHER)
    public R<CategoryQuestionCount> categoryWithTotalQuestionCount() throws IOException {
        return R.ok(this.questionService.categoryWithTotalQuestionCount(SecurityContextHolder.getOrgId()));
    }

    /**
     * 试题计数
     * @return
     */
    @PostMapping("/question/count")
    //@RequiresPermissions("question:countCategory")
    //@Log(title = "试题分类计数", businessType = BusinessType.OTHER)
    public R<QuestionCountResponse> count(@RequestBody @Validated QuestionCountRequest countRequest) throws IOException {
        countRequest.setOrgId(SecurityContextHolder.getOrgId());
        return R.ok(this.questionService.count(countRequest));
    }

    /**
     * 智能挑题（随机）
     * @param params
     * @return
     */
    @PostMapping("/question/smartChoose")
    //@RequiresPermissions("question:smartChoose")
    //@Log(title = "智能挑题", businessType = BusinessType.OTHER)
    public R<List<QuestionSearchResult>> smartChooseQuestions(@RequestBody @Validated SmartChooseQuestionRequest params) throws IOException {
        params.setOrgId(SecurityContextHolder.getOrgId());
        return R.ok(this.questionService.smartChooseQuestions(params));
    }

}
