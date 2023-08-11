package cn.qbs.wa.teach.exam.common.correct.corrector;

import cn.qbs.wa.teach.exam.common.enumerate.QuestionTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 试题批改类工厂
 * @Author zcm
 * @Date 2021-12-22 16:47
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class CorrectorFactory {

    private final ApplicationContext applicationContext;


    public Corrector getCorrector(Long questionTypeId) {
        if (QuestionTypeEnum.DAN_XUAN_TI.getId() == questionTypeId) {
            return applicationContext.getBean(DanXuanTiCorrector.class);
        }
        if (QuestionTypeEnum.DUO_XUAN_TI.getId() == questionTypeId) {
            return applicationContext.getBean(DuoXuanTiCorrector.class);
        }
        if (QuestionTypeEnum.PAN_DUAN_TI.getId() == questionTypeId) {
            return applicationContext.getBean(PanDuanTiCorrector.class);
        }
        // 填空题自动批改还不够完善，暂时不开放
        /*if (QuestionTypeEnum.TIAN_KONG_TI.getId() == questionTypeId) {
            return applicationContext.getBean(TianKongTiCorrector.class);
        }*/

        return null;
    }

}
