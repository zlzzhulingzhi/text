package cn.qbs.wa.teach.cert.pojo.standardtemplate;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/1/25 9:01
 */
@Data
public class StandardTemplateCheckCertRequest {

    @ApiModelProperty(value = "证书编号规则")
    private String certNumRule;
}
