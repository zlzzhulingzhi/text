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
public class DefaultExcelScreenAttendClassValidate implements ScreenAttendClassDataValidate {

    protected Set<String> errorReasons = new HashSet<>();

    protected ScreenAttendClassExcelData screenAttendClassExcelData;

    private String mark;

    //public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
    public static final String REGEX_MOBILE = "^((13[0-9])|(14[01｜(4-9)])|(15[(0-3)|(5-9)])|(16[2567])|(17[0-8])|(18[0-9])|(15[(0-3)|(5-9))]))\\d{8}$";


    /**
     * 验证入口
     * @param
     */
    @Override
    public void validate(ScreenAttendClassExcelData screenAttendClassExcelData) {
        if (screenAttendClassExcelData == null) {
            addErrorReason("信息不能为空！");
            return;
        }
        this.screenAttendClassExcelData = screenAttendClassExcelData;
        validateOrgName();
        validateClazzName();
        validateStudentNum();
        validateClassroom();
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

    private void validateOrgName() {
        String orgName= screenAttendClassExcelData.getOrgName();
        if (StringUtils.isBlank(orgName)) {
            addErrorReason("机构名称不能为空！");
            return;
        }
        screenAttendClassExcelData.setOrgName(orgName.trim());
    }

    private void validateClazzName() {
        String clazzName= screenAttendClassExcelData.getClazzName();
        if (StringUtils.isBlank(clazzName)) {
            addErrorReason("班级名称不能为空！");
            return;
        }
        screenAttendClassExcelData.setClazzName(clazzName.trim());
    }

    private void validateStudentNum() {
        Integer studentNum= screenAttendClassExcelData.getStudentNum();
        if (studentNum==null) {
            addErrorReason("学员人数不能为空！");
            return;
        }
        if (studentNum < Constants.DB_FAIL) {
            addErrorReason("学员人数不能负数！");
            return;
        }
    }

    private void validateClassroom() {
        String classroom= screenAttendClassExcelData.getClassroom();
        if (StringUtils.isBlank(classroom)) {
            addErrorReason("教室不能为空！");
            return;
        }
        screenAttendClassExcelData.setClassroom(classroom.trim());
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

