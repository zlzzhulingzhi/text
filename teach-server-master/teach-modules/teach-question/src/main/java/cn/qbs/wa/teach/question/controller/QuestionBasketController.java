package cn.qbs.wa.teach.question.controller;


import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.question.pojo.question.QuestionGroupResponse;
import cn.qbs.wa.teach.question.pojo.question.basket.QuestionBasketAddRequest;
import cn.qbs.wa.teach.question.pojo.question.basket.QuestionBasketGroupCountItem;
import cn.qbs.wa.teach.question.pojo.question.basket.QuestionBasketRemoveRequest;
import cn.qbs.wa.teach.question.service.QuestionBasketService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;


/**
 * 试题篮控制层
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-25 09:02:43
 */
@RestController
@RequestMapping("question/basket")
public class QuestionBasketController {

    /**
     * 服务对象
     */
    @Resource
    private QuestionBasketService questionBasketService;


    /**
     * 加入试题篮
     *
     * @param params
     * @return 
     */
    @PostMapping("add")
    //@RequiresPermissions("questionBasket:add")
    //@Log(title = "加入试题篮", businessType = BusinessType.INSERT)
    public R add(@RequestBody @Validated QuestionBasketAddRequest params) {
        this.questionBasketService.add(params);
        return R.ok("加入成功！");
    }

    /**
     * 获取试题篮试题ID列表
     *
     * @return
     */
    @PostMapping("questionIdList")
    //@RequiresPermissions("questionBasket:questionIdList")
    //@Log(title = "获取试题篮试题ID列表", businessType = BusinessType.OTHER)
    public R<List<Long>> questionIdList() {
        return R.ok(this.questionBasketService.getQuestionIdList());
    }

    /**
     * 试题篮分组统计
     * @return
     */
    @PostMapping("groupCount")
    //@RequiresPermissions("questionBasket:group")
    //@Log(title = "试题篮分组统计", businessType = BusinessType.OTHER)
    public R<List<QuestionBasketGroupCountItem>> groupCount() {
        return R.ok(this.questionBasketService.groupCount());
    }

    /**
     * 试题篮详情
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("questionBasket:details")
    //@Log(title = "试题篮详情", businessType = BusinessType.OTHER)
    public R<List<QuestionGroupResponse>> detail() throws IOException {
        return R.ok(this.questionBasketService.detail());
    }

    /**
     * 移除试题篮
     *
     * @param params 
     * @return 
     */
    @PostMapping("remove")
    //@RequiresPermissions("questionBasket:remove")
    //@Log(title = "移除试题篮", businessType = BusinessType.DELETE)
    public R remove(@RequestBody @Validated QuestionBasketRemoveRequest params) {
        this.questionBasketService.remove(params.getQuestionIdList());
        return R.ok("移除成功！");
    }

    /**
     * 清空试题篮
     * @return
     */
    @PostMapping("empty")
    //@RequiresPermissions("questionBasket:empty")
    //@Log(title = "清空试题篮", businessType = BusinessType.DELETE)
    public R empty() {
        this.questionBasketService.empty();
        return R.ok("试题篮已清空！");
    }
    
}

