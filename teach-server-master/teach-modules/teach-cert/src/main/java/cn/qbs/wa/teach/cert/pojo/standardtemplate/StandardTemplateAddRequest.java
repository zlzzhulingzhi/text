package cn.qbs.wa.teach.cert.pojo.standardtemplate;


import cn.qbs.wa.teach.cert.pojo.common.CommonConfigDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 证书模板(StandardTemplate)创建证书模板参数
 *
 * @author makejava
 * @since 2022-01-19 11:38:22
 */
@Data
public class StandardTemplateAddRequest {


    @ApiModelProperty(value = "证书模板名称")
    @NotBlank(message = "证书名称不能为空")
    private String name;

    @ApiModelProperty(value = "0 禁用 1正常")
    @NotNull(message = "状态不能为空")
    private Integer enabled;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "背景图片地址")
    @JsonProperty("backgroundTempUrl")
    @NotBlank(message = "背景图片不能为空")
    private String backgroundUrl;

    @ApiModelProperty(value = "模式 1 2")
    @NotNull(message = "模式不能为空")
    private Integer modelType;

    @ApiModelProperty(value = "证书信息配置")
    private CommonConfigDTO certTemplateConfig;


}

