package cn.qbs.wa.teach.question.validate.excel;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Excel单选题验证接口
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-11 11:16:43
 */
@Component
@Scope("prototype")
public class ExcelDuoXuanTiValidate extends AbstractExcelXuanZeTiValidate {

    @Override
    protected void validateOther() {
        validateOptionsCount();
        validateAnswer();
    }

    /**
     * 验证答案
     */
    protected void validateAnswer() {
        String questionAnswer = excelQuestion.getAnswer();
        if (StringUtils.isBlank(questionAnswer)) {
            addErrorReason("答案不能为空！");
            return;
        }

        String[] answers = questionAnswer.split("");
        if (ArrayUtils.isEmpty(answers)) {
            addErrorReason(String.format("答案[%s]格式不正确！", questionAnswer));
        }

        Map<String, String> optionMap = getOptionMap();
        for (String answer : answers) {
            if (!optionMap.containsKey(answer)) {
                addErrorReason(String.format("答案[%s]不在选项中！", answer));
                return;
            }
        }
    }
    
}

