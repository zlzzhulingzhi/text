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
public class DanXuanTiValidate extends DefaultQuestionValidate {

    public static final String QUESTION_TYPE_NAME = "【单选题】";
    
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
        if (StringUtils.isBlank(question.getAnswer())) {
            throw new IllegalParamsException(QUESTION_TYPE_NAME + "答案不能为空！");
        }

        String[] options = question.getOptions().stream().map(item -> item.getOption()).toArray(String[]::new);
        if (!ArrayUtils.contains(options, question.getAnswer())) {
            throw new IllegalParamsException(QUESTION_TYPE_NAME + "答案不在选项中！");
        }
    }
    
}

