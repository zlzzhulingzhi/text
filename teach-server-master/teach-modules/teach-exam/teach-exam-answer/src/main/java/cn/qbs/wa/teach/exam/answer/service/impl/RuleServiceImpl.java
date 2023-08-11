package cn.qbs.wa.teach.exam.answer.service.impl;

import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.exam.answer.mapper.RuleMapper;
import cn.qbs.wa.teach.exam.answer.service.RuleService;
import cn.qbs.wa.teach.exam.common.entity.Rule;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 考试规则(Rule)表服务实现类
 *
 * @author zcm
 * @since 2021-12-14 11:43:33
 */
@Slf4j
@Service("ruleService")
public class RuleServiceImpl extends ServiceImpl<RuleMapper, Rule> implements RuleService {

    @Override
    public Rule selectOneByCode(String ruleCode) {
        if (StringUtils.isNotEmpty(ruleCode)) {
            return this.lambdaQuery().eq(Rule::getCode, ruleCode).last("limit 1").one();
        }
        return null;
    }

    @Override
    public Rule selectOneByExamIdAndCode(Long examId, String ruleCode) {
        return getBaseMapper().selectOneByExamIdAndCode(examId, ruleCode);
    }

}

