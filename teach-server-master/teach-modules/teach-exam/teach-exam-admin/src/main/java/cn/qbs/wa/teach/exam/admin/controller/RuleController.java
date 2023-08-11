package cn.qbs.wa.teach.exam.admin.controller;


import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.exam.admin.pojo.exam.ExamRuleConfigDTO;
import cn.qbs.wa.teach.exam.admin.service.RuleService;
import cn.qbs.wa.teach.exam.common.entity.Rule;
import cn.qbs.wa.teach.exam.common.enumerate.RuleTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 规则控制层
 *
 * @author zcm
 * @version 1.0
 * @date 2021-12-15 14:42:49
 */
@RestController
@RequestMapping("rule")
@RequiredArgsConstructor
public class RuleController {

    private final RuleService ruleService;

    /**
     * 防作弊规则列表
     *
     * @return
     */
    @PostMapping("/antiCheating/list")
    //@RequiresPermissions("rule:antiCheating:list")
    //@Log(title = "防作弊规则列表", businessType = BusinessType.OTHER)
    public R<List<Rule>> antiCheatingList() {
        // 只查防作弊规则，过滤掉试题规则
        return R.ok(this.ruleService.list(SecurityContextHolder.getOrgId(), RuleTypeEnum.ANTI_CHEATING_RULE.getType()));
    }

    /**
     * 当前机构所有规则分组
     *
     * @return
     */
    @PostMapping("/group")
    //@RequiresPermissions("rule:group")
    //@Log(title = "当前机构所有规则", businessType = BusinessType.OTHER)
    public R<List<ExamRuleConfigDTO>> group() {
        return R.ok(this.ruleService.getExamRuleConfig(SecurityContextHolder.getOrgId()));
    }

    /**
     * 为机构添加考试规则
     * @param params
     * @return
     */
    @PostMapping("/addRulesToOrg")
    //@RequiresPermissions("rule:addRulesToOrg")
    //@Log(title = "为机构添加考试规则", businessType = BusinessType.OTHER)
    public R addRulesToOrg(@RequestBody @Validated IdRequest params) {
        this.ruleService.addRulesToOrg(params.getId());
        return R.ok();
    }

}

