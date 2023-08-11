package cn.qbs.wa.teach.question.validate.excel;

import cn.qbs.wa.teach.question.pojo.question.ExcelQuestion;

import java.util.Set;

/**
 * Excel试题验证接口
 * @Author zcm
 * @Date 2021/11/10 16:00
 * @Version 1.0
 */
public interface ExcelQuestionValidate {

    /**
     * Excel试题校验
     * @param excelQuestion
     */
    void validate(ExcelQuestion excelQuestion);

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
