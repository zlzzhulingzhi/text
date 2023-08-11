package cn.qbs.wa.teach.organization.excel;

import cn.qbs.wa.teach.common.security.annotation.EncodeContent;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 学员(Student)学员详情
 *
 * @author makejava
 * @since 2022-02-28 10:40:52
 */
@Data
public class StudentExcelDTO {

    @ExcelProperty(value = "学员姓名", index = 0)
    private String realName;

    @ExcelProperty(value = "手机号码", index = 1)
    private String phone;

    @ExcelProperty(value = "身份号码", index = 2)
    private String idNumber;

    @ExcelProperty(value = "性别", index = 3)
    private String sex;

    @ExcelProperty(value = "工作单位", index = 4)
    private String workUnit;

    @ExcelProperty(value = "学历", index = 5)
    private String education;

    @ExcelProperty(value = "籍贯", index = 6)
    private String nativePlace;

    @ExcelProperty(value = "毕业院校", index = 7)
    private String school;

    @ExcelProperty(value = "出生年月", index = 8)
    private String birthday;

    @ExcelProperty(value = "民族", index = 9)
    private String nation;

    @ExcelProperty(value = "工作地址", index = 10)
    private String workAddress;

    @ExcelProperty(value = "邮箱", index = 11)
    private String email;

}

