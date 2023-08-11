package cn.qbs.wa.teach.question.validate;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 单选题验证接口
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-04 11:10:43
 */
@Component
@Scope("prototype")
public class DuoXuanTiValidate extends DefaultQuestionValidate {

    public static final String QUESTION_TYPE_NAME = "【多选题】";
    
    @Override
    protected void validateOther() {
        validateOptions();
        validateAnswer();
    }

    /**
     * 验证选项
     */
    protected void validateOptions() {
        if (CollectionUtils.isEmpty(question.getOptions())) {
            throw new IllegalParamsException(QUESTION_TYPE_NAME + "选项不能为空！");
        }
    }

    /**
     * 验证答案
     */
    protected void validateAnswer() {
        String questionAnswer = question.getAnswer();
        if (StringUtils.isBlank(questionAnswer)) {
            throw new IllegalParamsException(QUESTION_TYPE_NAME + "答案不能为空！");
        }

        String[] answers = questionAnswer.split("");
        if (ArrayUtils.isEmpty(answers)) {
            throw new IllegalParamsException(String.format("%s答案[%s]格式不正确！", QUESTION_TYPE_NAME, questionAnswer));
        }

        String[] options = question.getOptions().stream().map(item -> item.getOption()).toArray(String[]::new);
        for (String answer : answers) {
            if (!ArrayUtils.contains(options, answer)) {
                throw new IllegalParamsException(String.format("%s答案[%s]不在选项中！", QUESTION_TYPE_NAME, answer));
            }
        }
    }
    
}

