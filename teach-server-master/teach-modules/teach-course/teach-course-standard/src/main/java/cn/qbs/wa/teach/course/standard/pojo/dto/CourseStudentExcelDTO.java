package cn.qbs.wa.teach.course.standard.pojo.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 课程学生 Excel 导出DTO
 * @author lance
 * @date 2021/11/24
 */
@Data
public class CourseStudentExcelDTO{

    // 用户名称 ｜ 手机号码 ｜ 报名时间 ｜ 开始学/学完/总节数 ｜ 最后一次学习时间

    @ExcelProperty(value = "学员姓名", index = 0)
    private String username;

    @ExcelProperty(value = "手机号码", index = 1)
    private String phoneNum;

    @ExcelProperty(value = "报名时间",index = 2)
    private String signUpTime;

    @ExcelProperty(value = "账号状态",index = 3)
    private String enabled;

    /*@ExcelProperty(value = "所在组织",index = 4)
    private String deptName;*/

    @ExcelProperty(value = "学员标签",index = 4)
    private String groupNames;

    //@ExcelProperty(value = "开始学/学完/总节数", index = 3)
    //private String learnCount;

//    @ExcelProperty(value = "最后一次学习时间", index = 4)
//    private String lastStudyTime;

   /* @ExcelProperty(value = "完成度", index = 6)
    private String learningRate;*/
}
