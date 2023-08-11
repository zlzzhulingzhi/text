package cn.qbs.wa.teach.exam.answer.mapper;

import cn.qbs.wa.teach.exam.common.entity.Rule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 考试规则(Rule)表数据库访问层
 *
 * @author zcm
 * @since 2021-12-14 11:43:33
 */
public interface RuleMapper extends BaseMapper<Rule> {

    Rule selectOneByExamIdAndCode(@Param("examId") Long examId, @Param("ruleCode") String ruleCode);

}

