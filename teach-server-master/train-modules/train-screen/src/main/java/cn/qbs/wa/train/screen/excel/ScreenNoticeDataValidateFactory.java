package cn.qbs.wa.train.screen.excel;


import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScreenNoticeDataValidateFactory {

    private final ApplicationContext applicationContext;


    public ScreenNoticeDataValidate getExcelScreenNoticeValidate() {


        return applicationContext.getBean("defaultExcelScreenNoticeValidate", DefaultExcelScreenNoticeValidate.class);
    }

}
