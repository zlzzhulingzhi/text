package cn.qbs.wa.train.screen.excel;

import java.util.Set;

public interface ScreenStatDataDynamicDataValidate {

    /**
     * Excel 学员导入验证
     * @param
     */
    void validate(ScreenStatDataDynamicExcelData screenStatDataDynamicExcelData);

    void setMark(String mark);

    /**
     * 是否校验通过
     * @return
     */
    boolean passed();


    /**
     * 获取错误原因
     * @return
     */
    Set<String> getErrorReasons();

}