package cn.qbs.wa.teach.exam.admin.pojo.tcert;

import cn.qbs.wa.teach.cert.api.pojo.DTO.template.CommonConfigDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 证书(Cert)证书详情
 *
 * @author makejava
 * @since 2022-01-19 19:19:23
 */
@Data
public class CertDetailResponse {

    @ApiModelProperty(value = "任务表中的证书数据库id")
    private Long id;

    @ApiModelProperty(value = "模板id")
    private Long templateId;

    @ApiModelProperty(value = "证书名称")
    private String name;

    @ApiModelProperty(value = "证书模板名称")
    private String templateName;

    @ApiModelProperty(value = "任务id")
    private Long taskId;

    @ApiModelProperty(value = "关联的证书id")
    private Long certId;

    @ApiModelProperty(value = "证书获取条件")
    private String ruleCode;

    @ApiModelProperty(value = "规则编码")
    private String ruleValue;

    @ApiModelProperty(value = "证书获取条件")
    private String text;

    @ApiModelProperty(value = "证书图片路径")
    private String certImageUrl;

    @ApiModelProperty(value = "是否获取证书标识 0:未获取 1:已获取")
    private Integer flag = 0;

    @ApiModelProperty(value = "证书搜索配置")
    private CommonConfigDTO certSearchConfig;

    @ApiModelProperty(value = "证书信息配置")
    private CommonConfigDTO certTemplateConfig;



}

