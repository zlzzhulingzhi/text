package cn.qbs.wa.teach.cert.api.pojo.DTO.template;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/1/19 14:12
 */
@Data
public class CommonConfigDetailLiteDTO {


    @ApiModelProperty(value = "字段编码")
    private String code;

    @ApiModelProperty(value = "字段名称")
    private String keyName;

    @ApiModelProperty(value = "字段值")
    private String keyValue;


}
