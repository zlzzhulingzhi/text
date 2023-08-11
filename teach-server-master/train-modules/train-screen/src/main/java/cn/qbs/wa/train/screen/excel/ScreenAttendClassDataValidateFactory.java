package cn.qbs.wa.train.screen.excel;


import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScreenAttendClassDataValidateFactory {

    private final ApplicationContext applicationContext;


    public ScreenAttendClassDataValidate getExcelScreenAttendClassValidate() {


        return applicationContext.getBean("defaultExcelScreenAttendClassValidate", DefaultExcelScreenAttendClassValidate.class);
    }

}
