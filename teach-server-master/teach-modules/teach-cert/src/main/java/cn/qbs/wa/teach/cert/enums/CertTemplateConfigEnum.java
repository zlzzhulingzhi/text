package cn.qbs.wa.teach.cert.enums;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CertTemplateConfigEnum {


    CERT_NAME("certName", "证书标题", "", 1, true, false, "input", "请输入证书标题", 10,null),

    USER_NAME("userName", "姓名", "", 2, true, false, null, null, null,null),

    HEAD_IMG("headImg", "头像", "", 2, false, true, null, null, null,null),

    ID_NUM("idNum", "身份证号码", "", 2, false, true, null, "44xxxxxxxxxxxxxxxx", null,null),

    EXAM_INFO("examInfo", "考试信息", "", 5, false, true, "hidden", null, null,null),

    SEARCH_ADDRESS("searchAddress", "查询网址", "", 5, true, true, "input", "请输入查询网址", null,null),

    CERT_CONTENT("certContent", "内容信息", "", 4, false, true, "input", "请输入内容信息", null,null),

    CERT_NUM_RULE("certNumRule", "证书编号", "", 6, true, false, "input", "证书编号前缀", null,null),

    AWARD_COMPANY("awardCompany", "发证单位", "", 3, true, true, "input", "请输入发证单位", 20,null),

    AWARD_DATE("awardDate", "发证日期", "", 2, true, true, "datepicker", "请选择发证日期", null,null),

    BACKGROUND_URL("backgroundUrl","背景图片","", 2, true, false, "hidden", null, null,null);

    @ApiModelProperty(value = "字段编码")
    private final String code;

    @ApiModelProperty(value = "字段名称", hidden = true)
    private final String keyName;

    @ApiModelProperty(value = "字段值")
    private final String keyValue;

    @ApiModelProperty(value = "排序")
    private final Integer sort;

    @ApiModelProperty(value = "是否被选择")
    private final Boolean selected;

    @ApiModelProperty(value = "能否被编辑")
    private final Boolean editable;

    @ApiModelProperty(value = "额外操作")
    private final String extraOperation;

    @ApiModelProperty(value = "提示语")
    private final String placeholder;

    @ApiModelProperty(value = "最大输入长度")
    private final Integer maxlength;

    @ApiModelProperty(value = "自定义样式")
    private final String customStyle;

    public static CertTemplateConfigEnum getEnumByCode(String code) {
        if (code == null) {
            return null;
        }
        for (CertTemplateConfigEnum certTemplateConfigEnum : CertTemplateConfigEnum.values()) {
            if (certTemplateConfigEnum.getCode().equals(code)) {
                return certTemplateConfigEnum;
            }
        }
        return null;
    }
}
