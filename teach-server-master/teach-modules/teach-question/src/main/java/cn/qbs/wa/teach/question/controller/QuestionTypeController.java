package cn.qbs.wa.teach.question.controller;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.domain.EnableRequest;
import cn.qbs.wa.teach.question.pojo.SelectOption;
import cn.qbs.wa.teach.question.pojo.question.type.*;
import cn.qbs.wa.teach.question.service.QuestionTypeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 题型(QuestionType)表控制层
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 13:35:24
 */
@RestController
@RequestMapping("questionType")
public class QuestionTypeController {

    /**
     * 服务对象
     */
    @Resource
    private QuestionTypeService questionTypeService;


    /**
     * 新增数据
     *
     * @param params 实体对象
     * @return 新增结果
     */
    @PostMapping("add")
    //@RequiresPermissions("questionType:add")
    //@Log(title = "新增题型", businessType = BusinessType.INSERT)
    public R<Long> add(@RequestBody @Validated QuestionTypeAddRequest params) {
        return R.ok(this.questionTypeService.add(params));
    }

    /**
     * 分页查询所有数据
     *
     * @param params 参数
     * @return 所有数据
     */
    @PostMapping("page")
    //@RequiresPermissions("questionType:page")
    //@Log(title = "分页查询题型", businessType = BusinessType.OTHER)
    public R<IPage<QuestionTypePageResponse>> page(@RequestBody QuestionTypePageRequest params) {
        return R.ok(this.questionTypeService.page(params));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param request
     * @return 单条数据
     */
    @PostMapping("detail")
    //@RequiresPermissions("questionType:details")
    //@Log(title = "题型详情", businessType = BusinessType.OTHER)
    public R<QuestionTypeDetailResponse> details(@RequestBody @Validated IdRequest request) {
        return R.ok(this.questionTypeService.detail(request.getId()));
    }

    /**
     * 修改题型
     *
     * @param params 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    //@RequiresPermissions("questionType:update")
    //@Log(title = "更新题型", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated QuestionTypeUpdateRequest params) {
        return R.ok(this.questionTypeService.update(params));
    }

    /**
     * 启用停用题型
     *
     * @param request
     * @return
     */
    @PostMapping("enable")
    //@RequiresPermissions("questionType:enable")
    //@Log(title = "启用停用题型", businessType = BusinessType.UPDATE)
    public R<Boolean> enable(@RequestBody @Validated EnableRequest request) {
        return R.ok(this.questionTypeService.enable(request));
    }

    /**
     * 获取前端下拉选择数据
     *
     * @return 所有数据
     */
    @PostMapping("selectOptionList")
    //@RequiresPermissions("questionType:selectOptionList")
    //@Log(title = "获取前端下拉选择题型数据", businessType = BusinessType.OTHER)
    public R<List<SelectOption>> selectOptionList() {
        List<SelectOption> selectOptionList = this.questionTypeService.getSelectOptionList();
        return R.ok(selectOptionList);
    }

}

