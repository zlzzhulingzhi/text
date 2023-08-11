package cn.qbs.wa.teach.cert.enums;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CertSearchConfigEnum {


    USER_NAME("userName", "姓名","",1,true,false),

    CERT_NUM("certNum", "证书编号","",1,false,true),

    ID_NUM("idNum", "身份证号码","",2,true,true),

    PHONE("phone", "手机号码","",2,false,true);


    @ApiModelProperty(value = "字段编码")
    private final String code;

    @ApiModelProperty(value = "字段名称",hidden = true)
    private final String keyName;

    @ApiModelProperty(value = "字段值")
    private final String keyValue;

    @ApiModelProperty(value = "排序")
    private final Integer sort;

    @ApiModelProperty(value = "是否被选择")
    private final Boolean selected;

    @ApiModelProperty(value = "能否被编辑")
    private final Boolean editable;

    public static CertSearchConfigEnum getEnumByCode(String code) {
        if (code == null) {
            return null;
        }
        for (CertSearchConfigEnum certSearchConfigEnum : CertSearchConfigEnum.values()) {
            if (certSearchConfigEnum.getCode().equals(code)) {
                return certSearchConfigEnum;
            }
        }
        return null;
    }

}
