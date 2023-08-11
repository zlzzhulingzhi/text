package cn.qbs.wa.train.logistics.pojo.clazzsign;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


@Data
public class ClazzSignExcelDTO {


    @ExcelProperty(value = "学员姓名", index = 0)
    private String studentName;

    @ExcelIgnore
    private String clazzName;

    @ExcelProperty(value = "签到日期",index = 1)
    private String signInDate;

    @ExcelProperty(value = "签到第几节课程",index = 2)
    private Integer lessonNum;

    @ExcelProperty(value = "签到状态",index = 3)
    private String signInStatus;


}
