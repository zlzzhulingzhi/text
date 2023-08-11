package cn.qbs.wa.train.logistics.excel;


import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnitStaffDataValidateFactory {

    private final ApplicationContext applicationContext;


    public UnitStaffDataValidate getExcelUnitStaffValidate(Long unitId) {


        return applicationContext.getBean("defaultExcelUnitStaffValidate", DefaultExcelUnitStaffValidate.class);
    }

}
