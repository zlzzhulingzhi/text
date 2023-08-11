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
public class CommonConfigDetailDTO {


    @ApiModelProperty(value = "字段编码")
    private String code;

    @ApiModelProperty(value = "字段名称", hidden = true)
    private String keyName;

    @ApiModelProperty(value = "字段值")
    private String keyValue;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "是否被选择")
    private Boolean selected;

    @ApiModelProperty(value = "额外操作")
    private String extraOperation;

    @ApiModelProperty(value = "输入框数")
    private Integer inputSize;

    @ApiModelProperty(value = "提示语")
    private String placeholder;

    @ApiModelProperty(value = "最大输入长度")
    private Integer maxlength;

    @ApiModelProperty(value = "能否被编辑")
    private Boolean editable;

    @ApiModelProperty(value = "自定义样式(json格式)")
    private String customStyle;

}
