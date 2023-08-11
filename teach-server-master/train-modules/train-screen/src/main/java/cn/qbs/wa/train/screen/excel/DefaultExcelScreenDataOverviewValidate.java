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
public class DefaultExcelScreenDataOverviewValidate implements ScreenDataOverviewDataValidate {

    protected Set<String> errorReasons = new HashSet<>();

    protected ScreenDataOverviewExcelData screenDataOverviewExcelData;

    private String mark;

    //public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
    public static final String REGEX_MOBILE = "^((13[0-9])|(14[01｜(4-9)])|(15[(0-3)|(5-9)])|(16[2567])|(17[0-8])|(18[0-9])|(15[(0-3)|(5-9))]))\\d{8}$";


    /**
     * 验证入口
     * @param
     */
    @Override
    public void validate(ScreenDataOverviewExcelData screenDataOverviewExcelData) {
        if (screenDataOverviewExcelData == null) {
            addErrorReason("信息不能为空！");
            return;
        }
        this.screenDataOverviewExcelData = screenDataOverviewExcelData;
        validateDisplayName();
        validateDisplayValue();
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

    private void validateDisplayName() {
        String displayName= screenDataOverviewExcelData.getDisplayName();
        if (StringUtils.isBlank(displayName)) {
            addErrorReason("数据名称不能为空！");
            return;
        }
        if ("开班课程".equals(displayName) && "培训人次".equals(displayName) && "入围机构".equals(displayName) && "资助机构".equals(displayName) && "资助班次".equals(displayName) && "资助人次".equals(displayName)) {
            addErrorReason("数据名称错误！");
            return;
        }
        screenDataOverviewExcelData.setDisplayName(displayName.trim());
    }

    private void validateDisplayValue() {
        String displayValue= screenDataOverviewExcelData.getDisplayValue();
        if (StringUtils.isBlank(displayValue)) {
            addErrorReason("数据值不能为空！");
            return;
        }
        if (Integer.valueOf(displayValue) < Constants.DB_FAIL) {
            addErrorReason("数据值不能为负数！");
            return;
        }
        screenDataOverviewExcelData.setDisplayValue(displayValue.trim());
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

