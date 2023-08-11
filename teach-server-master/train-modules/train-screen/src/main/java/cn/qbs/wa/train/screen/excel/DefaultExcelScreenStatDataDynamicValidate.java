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
public class DefaultExcelScreenStatDataDynamicValidate implements ScreenStatDataDynamicDataValidate {

    protected Set<String> errorReasons = new HashSet<>();

    protected ScreenStatDataDynamicExcelData screenStatDataDynamicExcelData;

    private String mark;

    //public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
    public static final String REGEX_MOBILE = "^((13[0-9])|(14[01｜(4-9)])|(15[(0-3)|(5-9)])|(16[2567])|(17[0-8])|(18[0-9])|(15[(0-3)|(5-9))]))\\d{8}$";


    /**
     * 验证入口
     * @param
     */
    @Override
    public void validate(ScreenStatDataDynamicExcelData screenStatDataDynamicExcelData) {
        if (screenStatDataDynamicExcelData == null) {
            addErrorReason("信息不能为空！");
            return;
        }
        this.screenStatDataDynamicExcelData = screenStatDataDynamicExcelData;
        validateDataName();
        validateUsingNum();
        validateFreeNum();
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

    private void validateDataName() {
        String dataName= screenStatDataDynamicExcelData.getDataName();
        if (StringUtils.isBlank(dataName)) {
            addErrorReason("数据名称不能为空！");
            return;
        }
        screenStatDataDynamicExcelData.setDataName(dataName.trim());
    }

    private void validateUsingNum() {
        Integer usingNum= screenStatDataDynamicExcelData.getUsingNum();
        if (usingNum==null) {
            addErrorReason("已使用数量不能为空！");
            return;
        }
        if (usingNum < Constants.DB_FAIL) {
            addErrorReason("已使用数量不能负数！");
            return;
        }
    }

    private void validateFreeNum() {
        Integer freeNum= screenStatDataDynamicExcelData.getFreeNum();
        if (freeNum==null) {
            addErrorReason("空闲数量不能为空！");
            return;
        }
        if (freeNum < Constants.DB_FAIL) {
            addErrorReason("空闲数量不能负数！");
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

