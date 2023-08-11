package cn.qbs.wa.train.screen.excel;


import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScreenStatDataDynamicDataValidateFactory {

    private final ApplicationContext applicationContext;


    public ScreenStatDataDynamicDataValidate getExcelScreenStatDataDynamicValidate() {


        return applicationContext.getBean("defaultExcelScreenStatDataDynamicValidate", DefaultExcelScreenStatDataDynamicValidate.class);
    }

}
