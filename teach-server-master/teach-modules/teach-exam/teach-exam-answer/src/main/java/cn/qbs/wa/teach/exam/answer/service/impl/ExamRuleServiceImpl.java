package cn.qbs.wa.teach.exam.answer.service.impl;

import cn.qbs.wa.teach.exam.answer.mapper.ExamRuleMapper;
import cn.qbs.wa.teach.exam.answer.service.ExamRuleService;
import cn.qbs.wa.teach.exam.common.entity.ExamRule;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 考试规则(ExamRule)表服务实现类
 *
 * @author zcm
 * @since 2021-12-14 11:43:32
 */
@Slf4j
@Service("examRuleService")
public class ExamRuleServiceImpl extends ServiceImpl<ExamRuleMapper, ExamRule> implements ExamRuleService {

}

