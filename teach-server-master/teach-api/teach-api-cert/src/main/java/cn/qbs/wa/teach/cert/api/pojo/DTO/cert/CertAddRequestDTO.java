package cn.qbs.wa.teach.cert.api.pojo.DTO.cert;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 证书(Cert)创建证书参数
 *
 * @author makejava
 * @since 2022-01-19 19:19:22
 */
@Data
public class CertAddRequestDTO {


    @ApiModelProperty(value = "模板id")
    @NotNull(message = "模板id不能为空")
    private Long templateId;

    @ApiModelProperty(value = "证书名称")
    @NotBlank(message = "证书名称不能为空")
    private String name;

    @ApiModelProperty(value = "0 禁用 1正常")
    @NotNull(message = "状态不能为空")
    private Integer enabled;

    @ApiModelProperty(value = "排序")
    private Integer sort;




}

