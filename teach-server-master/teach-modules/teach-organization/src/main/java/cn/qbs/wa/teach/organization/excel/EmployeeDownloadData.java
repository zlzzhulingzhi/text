package cn.qbs.wa.teach.organization.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 基础数据类
 *
 * @author Jiaju Zhuang
 **/
@Getter
@Setter
@EqualsAndHashCode
public class EmployeeDownloadData {

    // 用户名称	登陆密码	用户邮箱	手机号码	身份证号	性别   基础数据类.这里的排序和excel里面的排序一致

    @ExcelProperty("用户id")
    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
    private Long userId;

    @ExcelProperty("用户名称")
    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
    @ColumnWidth(12)
    private String realName;

    @ExcelProperty("手机号码")
    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
    @ColumnWidth(20)
    private String phone;

    @ExcelProperty("身份证号")
    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
    @ColumnWidth(20)
    private String idNumber;

    @ExcelProperty("性别")
    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
    private String sexName;

    @ExcelProperty("部门")
    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
    @ColumnWidth(12)
    String deptNames;

    @ExcelProperty("角色")
    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
    @ColumnWidth(12)
    String roleNames;
}
