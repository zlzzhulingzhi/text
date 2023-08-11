package cn.qbs.wa.train.logistics.excel;


import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClassroomDataValidateFactory {

    private final ApplicationContext applicationContext;


    public ClassroomDataValidate getExcelClassroomValidate() {


        return applicationContext.getBean("defaultExcelClassroomValidate", DefaultExcelClassroomValidate.class);
    }

}
