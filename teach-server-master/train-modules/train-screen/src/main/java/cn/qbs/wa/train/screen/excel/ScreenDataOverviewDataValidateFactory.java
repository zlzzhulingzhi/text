package cn.qbs.wa.train.screen.excel;


import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScreenDataOverviewDataValidateFactory {

    private final ApplicationContext applicationContext;


    public ScreenDataOverviewDataValidate getExcelScreenDataOverviewValidate() {


        return applicationContext.getBean("defaultExcelScreenDataOverviewValidate", DefaultExcelScreenDataOverviewValidate.class);
    }

}
