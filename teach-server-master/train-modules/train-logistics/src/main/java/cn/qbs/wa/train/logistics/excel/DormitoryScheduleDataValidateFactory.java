package cn.qbs.wa.train.logistics.excel;


import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DormitoryScheduleDataValidateFactory {

    private final ApplicationContext applicationContext;


    public DormitoryScheduleDataValidate getExcelDormitoryScheduleValidate() {


        return applicationContext.getBean("defaultExcelDormitoryScheduleValidate", DefaultExcelDormitoryScheduleValidate.class);
    }

}
