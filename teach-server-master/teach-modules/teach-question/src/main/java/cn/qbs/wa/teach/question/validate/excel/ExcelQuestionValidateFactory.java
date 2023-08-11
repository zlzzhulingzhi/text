package cn.qbs.wa.teach.question.validate.excel;

import cn.qbs.wa.teach.question.enumerate.QuestionTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Excel试题验证类工厂
 *
 * @Author zcm
 * @Date 2021/11/10 16:00
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ExcelQuestionValidateFactory {

    private final ApplicationContext applicationContext;


    public ExcelQuestionValidate getExcelQuestionValidate(String questionTypeName) {
        if (QuestionTypeEnum.DAN_XUAN_TI.getName().equals(questionTypeName)) {
            return applicationContext.getBean("excelDanXuanTiValidate", ExcelDanXuanTiValidate.class);
        }
        if (QuestionTypeEnum.DUO_XUAN_TI.getName().equals(questionTypeName)) {
            return applicationContext.getBean("excelDuoXuanTiValidate", ExcelDuoXuanTiValidate.class);
        }
        if (QuestionTypeEnum.PAN_DUAN_TI.getName().equals(questionTypeName)) {
            return applicationContext.getBean("excelPanDuanTiValidate", ExcelPanDuanTiValidate.class);
        }

        return applicationContext.getBean("defaultExcelQuestionValidate", DefaultExcelQuestionValidate.class);
    }

}
