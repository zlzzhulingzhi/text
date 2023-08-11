package cn.qbs.wa.train.logistics.excel;

import java.util.Set;

public interface EmployDataValidate {

    /**
     * Excel 学员导入验证
     * @param
     */
    void validate(EmployeeExcelData studentExcelData);

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