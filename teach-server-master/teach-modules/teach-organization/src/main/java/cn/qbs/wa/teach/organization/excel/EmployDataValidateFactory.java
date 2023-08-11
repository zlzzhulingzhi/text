package cn.qbs.wa.teach.organization.excel;


import cn.qbs.wa.teach.organization.entity.Employee;
import cn.qbs.wa.teach.organization.entity.Student;
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
