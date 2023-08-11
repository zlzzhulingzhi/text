package cn.qbs.wa.teach.question.validate.excel;

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
public class ExcelPanDuanTiValidate extends DefaultExcelQuestionValidate {

    private static final String[] ANSWER_ARRAY = {"对", "错"};


    @Override
    protected void validateOther() {
        validateAnswer();
    }

    /**
     * 验证答案
     */
    protected void validateAnswer() {
        if (StringUtils.isBlank(excelQuestion.getAnswer())) {
            addErrorReason("答案不能为空！");
            return;
        }

        excelQuestion.setAnswer(excelQuestion.getAnswer().trim());
        if (!ArrayUtils.contains(ANSWER_ARRAY, excelQuestion.getAnswer())) {
            addErrorReason("答案不在选项中！");
            return;
        }
    }
    
}

