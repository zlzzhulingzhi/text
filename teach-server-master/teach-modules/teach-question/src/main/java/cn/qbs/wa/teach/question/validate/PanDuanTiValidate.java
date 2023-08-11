package cn.qbs.wa.teach.question.validate;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 判断题验证接口
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-30 17:10:43
 */
@Component
@Scope("prototype")
public class PanDuanTiValidate extends DefaultQuestionValidate {

    public static final String QUESTION_TYPE_NAME = "【判断题】";

    private static final String[] ANSWER_ARRAY = {"对", "错"};
    
    @Override
    protected void validateOther() {
        validateAnswer();
    }


    /**
     * 验证答案
     */
    protected void validateAnswer() {
        if (StringUtils.isBlank(question.getAnswer())) {
            throw new IllegalParamsException(QUESTION_TYPE_NAME + "答案不能为空！");
        }

        question.setAnswer(question.getAnswer().trim());
        if (!ArrayUtils.contains(ANSWER_ARRAY, question.getAnswer())) {
            throw new IllegalParamsException(QUESTION_TYPE_NAME + "答案不在选项中！");
        }
    }
    
}

