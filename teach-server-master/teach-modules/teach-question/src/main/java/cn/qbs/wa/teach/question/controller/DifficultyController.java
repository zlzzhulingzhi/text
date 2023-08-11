package cn.qbs.wa.teach.question.controller;


import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.domain.EnableRequest;
import cn.qbs.wa.teach.question.pojo.SelectOption;
import cn.qbs.wa.teach.question.service.DifficultyService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * 难度(Difficulty)表控制层
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-09 10:42:49
 */
@RestController
@RequestMapping("difficulty")
public class DifficultyController {

    /**
     * 服务对象
     */
    @Resource
    private DifficultyService difficultyService;


    /**
     * 启用停用难度
     *
     * @param request
     * @return
     */
    @PostMapping("enable")
    //@RequiresPermissions("difficulty:enable")
    //@Log(title = "启用停用难度", businessType = BusinessType.UPDATE)
    public R<Boolean> enable(@RequestBody @Validated EnableRequest request) {
        return R.ok(this.difficultyService.enable(request));
    }

    /**
     * 获取前端下拉选择数据
     *
     * @return 所有数据
     */
    @PostMapping("/selectOptionList")
    //@RequiresPermissions("difficulty:selectOptionList")
    //@Log(title = "获取前端下拉选择题型数据", businessType = BusinessType.OTHER)
    public R<List<SelectOption>> selectOptionList() {
        List<SelectOption> selectOptionList = this.difficultyService.getSelectOptionList();
        return R.ok(selectOptionList);
    }
    
}

