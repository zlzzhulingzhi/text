package cn.qbs.wa.teach.organization.excel;


import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Excel学员验证类工厂
 *
 *
 */
@Component
@RequiredArgsConstructor
public class StudentDataValidateFactory {

    private final ApplicationContext applicationContext;


    public StudentDataValidate getExcelStudentValidate() {


        return applicationContext.getBean("defaultExcelStudentValidate", DefaultExcelStudentValidate.class);
    }

}
