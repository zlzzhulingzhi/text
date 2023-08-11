package cn.qbs.wa.teach.cert.pojo.cert;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 证书(Cert)更新证书参数
 *
 * @author makejava
 * @since 2022-01-19 19:19:23
 */
@Data
public class CertUpdateRequest {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "模板id")
    private Long templateId;

    @ApiModelProperty(value = "证书模板名称")
    private String name;

    @ApiModelProperty(value = "0 禁用 1正常")
    private Integer enabled;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "颁发数")
    private Integer awardCount;

    @ApiModelProperty(value = "背景图片地址")
    private String backgroundUrl;

    @ApiModelProperty(value = "模式 1 2")
    private Integer modelType;

    @ApiModelProperty(value = "模板名称")
    private String templateName;

}

