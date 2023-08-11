package cn.qbs.wa.teach.organization.excel;

import java.util.Set;

public interface StudentDataValidate {

    /**
     * Excel 学员导入验证
     * @param
     */
    void validate(StudentExcelData studentExcelData);

    /**
     * 是否校验通过
     * @return
     */
    boolean passed();

    /**
     * 设置标记
     * @param mark
     */
    void setMark(String mark);

    /**
     * 获取错误原因
     * @return
     */
    Set<String> getErrorReasons();

}