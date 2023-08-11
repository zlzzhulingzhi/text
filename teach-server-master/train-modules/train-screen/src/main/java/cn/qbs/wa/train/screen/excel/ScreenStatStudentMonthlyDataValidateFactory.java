package cn.qbs.wa.train.screen.excel;


import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScreenStatStudentMonthlyDataValidateFactory {

    private final ApplicationContext applicationContext;


    public ScreenStatStudentMonthlyDataValidate getExcelScreenStatStudentMonthlyValidate() {


        return applicationContext.getBean("defaultExcelScreenStatStudentMonthlyValidate", DefaultExcelScreenStatStudentMonthlyValidate.class);
    }

}
