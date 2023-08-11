package cn.qbs.wa.train.screen.excel;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
@Scope("prototype")
public class DefaultExcelScreenNoticeValidate implements ScreenNoticeDataValidate {

    protected Set<String> errorReasons = new HashSet<>();

    protected ScreenNoticeExcelData screenNoticeExcelData;

    private String mark;

    //public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
    public static final String REGEX_MOBILE = "^((13[0-9])|(14[01｜(4-9)])|(15[(0-3)|(5-9)])|(16[2567])|(17[0-8])|(18[0-9])|(15[(0-3)|(5-9))]))\\d{8}$";


    /**
     * 验证入口
     * @param
     */
    @Override
    public void validate(ScreenNoticeExcelData screenNoticeExcelData) {
        if (screenNoticeExcelData == null) {
            addErrorReason("信息不能为空！");
            return;
        }
        this.screenNoticeExcelData = screenNoticeExcelData;
        validateTitle();
        validateContent();
        validatePublishDate();
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

    private void validateTitle() {
        String title= screenNoticeExcelData.getTitle();
        if (StringUtils.isBlank(title)) {
            addErrorReason("标题不能为空！");
            return;
        }
        screenNoticeExcelData.setTitle(title.trim());
    }

    private void validateContent() {
        String content= screenNoticeExcelData.getContent();
        if (StringUtils.isBlank(content)) {
            addErrorReason("内容不能为空！");
            return;
        }
        screenNoticeExcelData.setContent(content.trim());
    }

    private void validatePublishDate() {
        String publishDate= screenNoticeExcelData.getPublishDate();
        if (StringUtils.isBlank(publishDate)) {
            addErrorReason("发布日期不能为空！");
            return;
        }
        screenNoticeExcelData.setPublishDate(publishDate.trim());
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

