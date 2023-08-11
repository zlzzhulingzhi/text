package cn.qbs.wa.teach.question.validate.excel;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Excel单选题验证接口
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-11 16:10:43
 */
@Component
@Scope("prototype")
public class ExcelDanXuanTiValidate extends AbstractExcelXuanZeTiValidate {

    @Override
    protected void validateOther() {
        validateOptionsCount();
        validateAnswer();
    }

    /**
     * 验证答案
     */
    protected void validateAnswer() {
        String answer = excelQuestion.getAnswer();
        if (StringUtils.isBlank(answer)) {
            addErrorReason("答案不能为空！");
            return;
        }

        Map<String, String> optionMap = getOptionMap();
        if (!optionMap.containsKey(answer)) {
            addErrorReason("答案不在选项中！");
            return;
        }
    }
    
}

