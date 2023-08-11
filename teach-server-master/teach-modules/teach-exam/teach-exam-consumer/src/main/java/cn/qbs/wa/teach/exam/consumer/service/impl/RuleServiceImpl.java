package cn.qbs.wa.teach.exam.consumer.service.impl;

import cn.qbs.wa.teach.exam.consumer.mapper.RuleMapper;
import cn.qbs.wa.teach.exam.consumer.service.RuleService;
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

}

