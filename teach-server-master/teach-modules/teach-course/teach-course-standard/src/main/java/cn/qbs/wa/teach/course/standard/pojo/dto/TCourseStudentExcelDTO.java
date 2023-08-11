package cn.qbs.wa.teach.course.standard.pojo.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 课程学生 Excel 导出DTO
 * @author lance
 * @date 2021/11/24
 */
@Data
public class TCourseStudentExcelDTO {

    @ExcelProperty(value = "学员姓名", index = 0)
    private String realName;

    @ExcelProperty(value = "手机号", index = 1)
    private String phone;

    @ExcelProperty(value = "课程名称",index = 2)
    private String courseName;

    @ExcelProperty(value = "报名时间",index = 3)
    private String signUpTime;

    @ExcelProperty(value = "确认状态",index = 4)
    private String status;
}
