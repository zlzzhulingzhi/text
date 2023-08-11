package cn.qbs.wa.train.logistics.excel;


import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployDataValidateFactory {

    private final ApplicationContext applicationContext;


    public EmployDataValidate getExcelEmployValidate(Long orgId) {


        return applicationContext.getBean("defaultExcelEmployValidate", DefaultExcelEmployValidate.class);
    }

}
