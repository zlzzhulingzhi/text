package cn.qbs.wa.teach.cert.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum TemplateConfigTypeEnum {


    CERT_CONFIG(1, "证书参数配置"),


    SEARCH_CONFIG(2, "证书查询配置");

    private final Integer id;

    private final String remark;


    public static TemplateConfigTypeEnum getEnumById(Integer id) {
        if (id == null) {
            return null;
        }
        TemplateConfigTypeEnum[] values = TemplateConfigTypeEnum.values();
        for (TemplateConfigTypeEnum templateConfigTypeEnum: values) {
            if(templateConfigTypeEnum.getId().equals(id)){
                return templateConfigTypeEnum;
            }
        }
        return null;
    }

}

