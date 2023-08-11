package cn.qbs.wa.teach.exam.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.exam.admin.mapper.RuleMapper;
import cn.qbs.wa.teach.exam.admin.pojo.exam.ExamRuleConfigDTO;
import cn.qbs.wa.teach.exam.admin.pojo.exam.ExamRuleConfigDetailDTO;
import cn.qbs.wa.teach.exam.admin.service.RuleService;
import cn.qbs.wa.teach.exam.common.entity.Rule;
import cn.qbs.wa.teach.exam.common.enumerate.RuleEnum;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public List<Rule> list(Long orgId, Integer type) {
        return this.lambdaQuery().eq(Rule::getOrgId, orgId).eq(type != null, Rule::getType, type).eq(Rule::getEnabled, true).list();
    }

    @Override
    public void addRulesToOrg(Long orgId) {
        List<Rule> addRuleList = Arrays.stream(RuleEnum.values()).map(e -> {
            Rule rule = new Rule();
            rule.setCode(e.getCode());
            rule.setType(e.getType());
            rule.setGroupCode(e.getGroupCode());
            rule.setRuleName(e.getRuleName());
            rule.setEnabled(true);
            rule.setOrgId(orgId);
            rule.setUiType(e.getUiType());
            return rule;
        }).collect(Collectors.toList());

        // 移除数据库中已经存在的
        SecurityContextHolder.setSelectOrgId(String.valueOf(orgId));
        List<Rule> dbOrgRuleList = this.lambdaQuery().eq(Rule::getOrgId, orgId).list();
        if (CollectionUtils.isNotEmpty(dbOrgRuleList)) {
            List<String> codeList = dbOrgRuleList.stream().map(i -> i.getCode()).collect(Collectors.toList());
            addRuleList.removeIf(i -> codeList.contains(i.getCode()));
        }

        if (CollectionUtils.isNotEmpty(addRuleList)) {
            this.saveBatch(addRuleList);
        }
    }

    @Override
    public Rule selectOneByCode(String code) {
        if (StringUtils.isNotEmpty(code)) {
            return this.lambdaQuery().eq(Rule::getCode, code).last("limit 1").one();
        }
        return null;
    }

    @Override
    public List<ExamRuleConfigDTO> getExamRuleConfig(Long orgId) {
        List<Rule> ruleList = this.list(orgId, null);
        if (CollectionUtils.isNotEmpty(ruleList)) {
            Map<Integer, List<Rule>> map = ruleList.stream().collect(Collectors.groupingBy(Rule::getType));
            List<ExamRuleConfigDTO> examRuleConfigDTOList = new ArrayList<>();
            map.forEach((k, list) -> {
                ExamRuleConfigDTO examRuleConfigDTO = new ExamRuleConfigDTO();
                examRuleConfigDTO.setType(k);

                List<ExamRuleConfigDetailDTO> examRuleConfigDetailDTOList = list.stream().map(rule -> {
                    ExamRuleConfigDetailDTO examRuleConfigDetailDTO = new ExamRuleConfigDetailDTO();
                    BeanUtil.copyProperties(rule, examRuleConfigDetailDTO);
                    if (RuleEnum.ALLOW_VIEWING_AFTER_EXAM.getCode().equals(rule.getCode())) {
                        examRuleConfigDetailDTO.setSelected(true);
                    } else {
                        examRuleConfigDetailDTO.setSelected(false);
                    }
                    return examRuleConfigDetailDTO;
                }).collect(Collectors.toList());
                examRuleConfigDTO.setRuleList(examRuleConfigDetailDTOList);

                examRuleConfigDTOList.add(examRuleConfigDTO);
            });
            return examRuleConfigDTOList;
        }
        return null;
    }

}

