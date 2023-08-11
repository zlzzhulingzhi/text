package cn.qbs.wa.teach.question.validate.excel;

import cn.qbs.wa.teach.question.constant.QuestionConst;
import cn.qbs.wa.teach.question.entity.Difficulty;
import cn.qbs.wa.teach.question.enumerate.QuestionTypeEnum;
import cn.qbs.wa.teach.question.pojo.question.ExcelQuestion;
import cn.qbs.wa.teach.question.service.DifficultyService;
import cn.qbs.wa.teach.question.util.CacheUtil;
import cn.qbs.wa.teach.question.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * 默认Excel试题验证接口
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-10 16:00:43
 */
@Component
@Scope("prototype")
public class DefaultExcelQuestionValidate implements ExcelQuestionValidate {

    protected Set<String> errorReasons = new HashSet<>();

    protected ExcelQuestion excelQuestion;

    @Resource
    private DifficultyService difficultyService;

    @Resource
    private CacheUtil cacheUtil;

    private String mark;

    /**
     * 验证入口
     * @param excelQuestion
     */
    @Override
    public void validate(ExcelQuestion excelQuestion) {
        if (excelQuestion == null) {
            addErrorReason("试题不能为空！");
            return;
        }
        this.excelQuestion = excelQuestion;
        validateQuestionName();
        validateContent();
        validateScore();

        validateDifficulty();
        validateQuestionCategory();
        validateOther();
    }

    @Override
    public boolean passed() {
        return CollectionUtils.isEmpty(this.errorReasons);
    }

    @Override
    public void setMark(String mark) {
        this.mark = mark;
    }

    @Override
    public Set<String> getErrorReasons() {
        return errorReasons;
    }

    /**
     * 添加错误原因
     */
    protected void addErrorReason(String errorReason) {
        if (StringUtils.isNotBlank(errorReason)) {
            this.errorReasons.add(msgFormat(errorReason));
        }
    }

    private void validateQuestionName() {
        String questionTypeName = excelQuestion.getQuestionTypeName();
        if (StringUtils.isBlank(questionTypeName)) {
            addErrorReason("试题题型不能为空！");
            return;
        }

        excelQuestion.setQuestionTypeName(questionTypeName.trim());
        QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromName(questionTypeName);
        if (questionTypeEnum == null) {
            addErrorReason("不支持的试题题型【" + questionTypeName + "】！");
            return;
        }
    }

    private void validateDifficulty() {
        if (StringUtils.isBlank(excelQuestion.getDifficultyName())) {
//            addErrorReason("试题难度不能为空！");
            excelQuestion.setDifficultyName(QuestionConst.DEFAULT_DIFFICULTY_NAME);
            return;
        }

        excelQuestion.setDifficultyName(excelQuestion.getDifficultyName().trim());
        String difficultyName = excelQuestion.getDifficultyName();
        Difficulty difficulty = cacheUtil.getFromBucket(RedisKeyUtil.getDifficultyNameKey(difficultyName),
                () -> difficultyService.lambdaQuery().eq(Difficulty::getName, difficultyName).last("limit 1").one()
        );

        if (difficulty == null) {
//            addErrorReason("不存在的试题难度[" + excelQuestion.getDifficultyName() + "]！");
            excelQuestion.setDifficultyName(QuestionConst.DEFAULT_DIFFICULTY_NAME);
            return;
        }
    }

    private void validateQuestionCategory() {
        if (StringUtils.isBlank(excelQuestion.getCategoryName())) {
            addErrorReason("试题分类不能为空！");
            return;
        }
        excelQuestion.setCategoryName(excelQuestion.getCategoryName().trim());
    }

    protected void validateContent() {
        if (StringUtils.isBlank(excelQuestion.getContent())) {
            addErrorReason("试题题干不能为空！");
            return;
        }
    }

    protected void validateScore() {
        if (excelQuestion.getScore() == null) {
//            addErrorReason("试题分数不能为空！");
//            return;
            excelQuestion.setScore(QuestionConst.DEFAULT_SCORE);
        } else {
            if (excelQuestion.getScore().compareTo(QuestionConst.MIN_SCORE) < 0) {
                addErrorReason("试题分数不能小于" + QuestionConst.MIN_SCORE + "！");
                return;
            } else if (excelQuestion.getScore().compareTo(QuestionConst.MAX_SCORE) > 0) {
                addErrorReason("试题分数不能大于" + QuestionConst.MAX_SCORE + "！");
                return;
            }
        }
    }

    protected String msgFormat(String msg) {
        if (StringUtils.isBlank(this.mark)) {
            return msg;
        }
        return String.format("【%s】%s", this.mark, msg);
    }

    /**
     * 其它验证，由子类复写
     */
    protected void validateOther() {
    }

}

