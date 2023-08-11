package cn.qbs.wa.teach.exam.admin.service;

import cn.qbs.wa.teach.exam.admin.pojo.exam.ExamRuleConfigDTO;
import cn.qbs.wa.teach.exam.common.entity.Rule;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 考试规则(Rule)表服务接口
 *
 * @author zcm
 * @since 2021-12-14 11:43:33
 */
public interface RuleService extends IService<Rule> {

    List<Rule> list(Long orgId, Integer type);

    void addRulesToOrg(Long orgId);

    Rule selectOneByCode(String code);

    List<ExamRuleConfigDTO> getExamRuleConfig(Long orgId);

}

