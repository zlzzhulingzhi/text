package cn.qbs.wa.teach.question.validate;

import cn.qbs.wa.teach.question.enumerate.QuestionTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 试题验证类工厂
 *
 * @Author zcm
 * @Date 2021/11/4 11:48
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class QuestionValidateFactory {

    private final ApplicationContext applicationContext;


    public QuestionValidate getQuestionValidate(Long questionTypeId) {
        if (QuestionTypeEnum.DAN_XUAN_TI.getId() == questionTypeId) {
            return applicationContext.getBean("danXuanTiValidate", DanXuanTiValidate.class);
        }
        if (QuestionTypeEnum.DUO_XUAN_TI.getId() == questionTypeId) {
            return applicationContext.getBean("duoXuanTiValidate", DuoXuanTiValidate.class);
        }
        if (QuestionTypeEnum.PAN_DUAN_TI.getId() == questionTypeId) {
            return applicationContext.getBean("panDuanTiValidate", PanDuanTiValidate.class);
        }

        return applicationContext.getBean("defaultQuestionValidate", DefaultQuestionValidate.class);
    }

}
