package cn.qbs.wa.teach.question.validate;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.question.constant.QuestionConst;
import cn.qbs.wa.teach.question.entity.Category;
import cn.qbs.wa.teach.question.entity.Difficulty;
import cn.qbs.wa.teach.question.enumerate.CategoryGroupEnum;
import cn.qbs.wa.teach.question.enumerate.QuestionTypeEnum;
import cn.qbs.wa.teach.question.pojo.question.BasicQuestion;
import cn.qbs.wa.teach.question.service.CategoryService;
import cn.qbs.wa.teach.question.service.DifficultyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 试题验证接口
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-04 11:10:43
 */
@Component
@Scope("prototype")
public class DefaultQuestionValidate implements QuestionValidate {

    protected BasicQuestion question;

    protected QuestionTypeEnum questionTypeEnum;

    @Resource
    private DifficultyService difficultyService;

    @Resource
    private CategoryService categoryService;


    /**
     * 验证入口
     * @param basicQuestion
     */
    @Override
    public void validate(BasicQuestion basicQuestion) {
        if (basicQuestion == null) {
            throw new IllegalParamsException("试题不能为空！");
        }
        this.question = basicQuestion;
        validateQuestionType();
        validateContent();
        validateScore();

        validateDifficulty();
        validateQuestionCategory();
        validateOther();
    }

    private void validateQuestionType() {
        if (question.getQuestionTypeId() == null) {
            throw new IllegalParamsException("试题题型不能为空！");
        }

        QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromId(question.getQuestionTypeId());
        if (questionTypeEnum == null) {
            throw new IllegalParamsException("不支持的试题题型！");
        }

        this.questionTypeEnum = questionTypeEnum;
    }

    private void validateDifficulty() {
        if (question.getDifficultyId() == null) {
            throw new IllegalParamsException(msgFormat("试题难度不能为空！"));
        }

        Long count = difficultyService.lambdaQuery().eq(Difficulty::getId, question.getDifficultyId()).count();
        if (count == null || count < 1) {
            throw new IllegalParamsException(msgFormat("不存在的试题难度[" + question.getDifficultyId() + "]！"));
        }
    }

    private void validateQuestionCategory() {
        if (CollectionUtils.isEmpty(question.getCategoryIds())) {
            throw new IllegalParamsException(msgFormat("试题分类不能为空！"));
        }

        question.setCategoryIds(question.getCategoryIds().stream().distinct().collect(Collectors.toList()));
        List<Long> categoryIdList = question.getCategoryIds();
        List<Category> categoryList = categoryService.lambdaQuery()
                .eq(Category::getGroupId, CategoryGroupEnum.QUESTION.getId())
                .in(Category::getId, categoryIdList)
                .list();
        if (categoryList.size() < categoryIdList.size()) {
            List<Long> dbCategoryIdList = categoryList.stream().map(c -> c.getId()).collect(Collectors.toList());
            categoryIdList.removeAll(dbCategoryIdList);
            throw new IllegalParamsException(msgFormat("不存在的分类 " + categoryIdList + "！"));
        }
    }

    protected void validateContent() {
        if (StringUtils.isBlank(question.getContent())) {
            throw new IllegalParamsException(msgFormat("试题题干不能为空！"));
        }
    }

    protected void validateScore() {
        if (question.getScore() == null) {
//            throw new IllegalParamsException(msgFormat("试题分数不能为空！"));
            question.setScore(QuestionConst.DEFAULT_SCORE);
        } else {
            if (question.getScore().compareTo(QuestionConst.MIN_SCORE) < 0) {
                throw new IllegalParamsException(msgFormat("试题分数不能小于" + QuestionConst.MIN_SCORE + "！"));
            } else if (question.getScore().compareTo(QuestionConst.MAX_SCORE) > 0) {
                throw new IllegalParamsException("试题分数不能大于" + QuestionConst.MAX_SCORE + "！");
            }
        }
    }

    protected String msgFormat(String msg) {
        if (questionTypeEnum == null) {
            return msg;
        }
        return String.format("【%s】%s", questionTypeEnum.getName(), msg);
    }

    /**
     * 其它验证，由子类复写
     */
    protected void validateOther() {
    }

}

