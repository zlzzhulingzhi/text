package cn.qbs.wa.train.screen.excel;

import cn.qbs.wa.teach.common.core.constant.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

@Component
@Scope("prototype")
public class DefaultExcelScreenStatStudentMonthlyValidate implements ScreenStatStudentMonthlyDataValidate {

    protected Set<String> errorReasons = new HashSet<>();

    protected ScreenStatStudentMonthlyExcelData screenStatStudentMonthlyExcelData;

    private String mark;

    //public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
    public static final String REGEX_MOBILE = "^((13[0-9])|(14[01｜(4-9)])|(15[(0-3)|(5-9)])|(16[2567])|(17[0-8])|(18[0-9])|(15[(0-3)|(5-9))]))\\d{8}$";


    /**
     * 验证入口
     * @param
     */
    @Override
    public void validate(ScreenStatStudentMonthlyExcelData screenStatStudentMonthlyExcelData) {
        if (screenStatStudentMonthlyExcelData == null) {
            addErrorReason("信息不能为空！");
            return;
        }
        this.screenStatStudentMonthlyExcelData = screenStatStudentMonthlyExcelData;
        validateYear();
        validateMonth();
        validateNum();
        validateOther();
    }

    @Override
    public boolean passed() {
        return CollectionUtils.isEmpty(this.errorReasons);
    }

    @Override
    public void setMark(String mark) {
        this.mark = mark;
    }

    @Override
    public Set<String> getErrorReasons() {
        return errorReasons;
    }

    /**
     * 添加错误原因
     */
    protected void addErrorReason(String errorReason) {
        if (StringUtils.isNotBlank(errorReason)) {
            this.errorReasons.add(msgFormat(errorReason));
        }
    }

    private void validateYear() {
       String year= screenStatStudentMonthlyExcelData.getYear();
        if (StringUtils.isBlank(year)) {
            addErrorReason("年份不能为空！");
            return;
        }
        if (Integer.valueOf(year) < Constants.DB_FAIL) {
            addErrorReason("年份不能为负数！");
            return;
        }
        screenStatStudentMonthlyExcelData.setYear(year.trim());
    }

    private void validateMonth() {
        Integer month= screenStatStudentMonthlyExcelData.getMonth();
        if (month==null) {
            addErrorReason("月份不能为空！");
            return;
        }
        if (month < Constants.DB_FAIL || month > 12) {
            addErrorReason("没有这月份！");
            return;
        }
    }

    private void validateNum() {
        Integer num= screenStatStudentMonthlyExcelData.getNum();
        if (num==null) {
            addErrorReason("学员人数不能为空！");
            return;
        }
        if (num < Constants.DB_FAIL) {
            addErrorReason("学员人数不能负数！");
            return;
        }
    }

    protected String msgFormat(String msg) {
        if (StringUtils.isBlank(this.mark)) {
            return msg;
        }
        return String.format("【%s】%s", this.mark, msg);
    }

    /**
     * 其它验证，由子类复写
     */
    protected void validateOther() {
    }

}

