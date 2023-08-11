package cn.qbs.wa.teach.question.controller;

import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdOrgRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.common.security.annotation.AccessAuth;
import cn.qbs.wa.teach.common.security.annotation.AutoSelectOrg;
import cn.qbs.wa.teach.question.entity.Category;
import cn.qbs.wa.teach.question.pojo.category.QuestionCategoryAddRequest;
import cn.qbs.wa.teach.question.pojo.question.QuestionDetailResponse;
import cn.qbs.wa.teach.question.service.CategoryService;
import cn.qbs.wa.teach.question.service.QuestionService;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 微服务内部请求接口
 *
 * @Author zcm
 * @Date 2022-09-09 08:51
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/inner")
public class InnerController {

    @Resource
    private QuestionService questionService;

    @Resource
    private CategoryService categoryService;
    /**
     * 查询试题详情
     *
     * @param request
     * @return
     */
    @PostMapping("/question/detail")
    @AccessAuth(SecurityConstants.INNER)
    @AutoSelectOrg
//    @Log(title = "试题详情", businessType = BusinessType.QUERY)
    public R<QuestionDetailResponse> questionDetail(@RequestBody @Validated IdOrgRequest request) {
        log.info("内部调用查询试题详情 {}", JSON.toJSONString(request));
        return R.ok(this.questionService.detail(request.getId()));
    }

    @ApiOperation(value = "初始化")
    @PostMapping("/init")
    //@Log(title = "初始化【课程分类】", businessType = BusinessType.INSERT)
    public R<Category> init(@RequestBody QuestionCategoryAddRequest params) {
        return R.ok(this.categoryService.init(params));
    }

}
