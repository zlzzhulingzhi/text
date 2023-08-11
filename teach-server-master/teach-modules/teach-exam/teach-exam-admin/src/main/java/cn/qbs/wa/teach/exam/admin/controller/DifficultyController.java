package cn.qbs.wa.teach.exam.admin.controller;


import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.exam.common.enumerate.DifficultyEnum;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 难度(Difficulty)表控制层
 *
 * @author zcm
 * @version 1.0
 * @date 2021-12-15 09:42:49
 */
@RestController
@RequestMapping("difficulty")
public class DifficultyController {

    /**
     * 获取难度列表
     *
     * @return 所有数据
     */
    @PostMapping("/list")
    //@RequiresPermissions("difficulty:list")
    //@Log(title = "获取难度列表", businessType = BusinessType.OTHER)
    public R<List<Map<String, Object>>> list() {
        return R.ok(DifficultyEnum.list());
    }

}

