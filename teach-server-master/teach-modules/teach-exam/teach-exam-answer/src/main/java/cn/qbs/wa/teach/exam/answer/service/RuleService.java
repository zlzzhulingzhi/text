package cn.qbs.wa.teach.exam.answer.service;

import cn.qbs.wa.teach.exam.common.entity.Rule;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 考试规则(Rule)表服务接口
 *
 * @author zcm
 * @since 2021-12-14 11:43:33
 */
public interface RuleService extends IService<Rule> {

    Rule selectOneByCode(String ruleCode);

    Rule selectOneByExamIdAndCode(Long examId, String ruleCode);
}

