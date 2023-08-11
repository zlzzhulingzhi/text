package cn.qbs.wa.teach.question.validate.excel;

import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.question.constant.QuestionConst;
import cn.qbs.wa.teach.question.pojo.question.ExcelQuestion;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Excel 选择题抽象验证器
 * @Author zcm
 * @Date 2022/1/11 19:55
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractExcelXuanZeTiValidate extends DefaultExcelQuestionValidate {

    public static final char START_OPTION = 'A';

    public static final char END_OPTION = 'J';


    protected Object invokeGetOptionMethod(Class<? extends ExcelQuestion> aClass, String option) {
        String methodName = "getOption" + option;
        try {
            Method method = aClass.getMethod(methodName);
            Object result = method.invoke(excelQuestion);
            return result;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        throw new ServiceException(String.format("调用%s method获取结果失败！", methodName));
    }

    /**
     * 验证有效选项个数
     */
    protected void validateOptionsCount() {
        Map<String, String> optionMap = getOptionMap();
        if (optionMap.size() < QuestionConst.MULTIPLE_CHOICE_QUESTION_MIN_OPTION_COUNT) {
            addErrorReason("选项不能少于" + QuestionConst.MULTIPLE_CHOICE_QUESTION_MIN_OPTION_COUNT + "个！");
        }
    }

    protected Map<String, String> getOptionMap() {
        Map<String, String> optionMap = new HashMap<>(4);
        Class<? extends ExcelQuestion> aClass = excelQuestion.getClass();
        for (char c = START_OPTION; c <= END_OPTION; c++) {
            String option = String.valueOf(c);
            String value = (String) invokeGetOptionMethod(aClass, option);
            if (StringUtils.isNotBlank(value)) {
                optionMap.put(option, value);
            }
        }
        return optionMap;
    }

}
