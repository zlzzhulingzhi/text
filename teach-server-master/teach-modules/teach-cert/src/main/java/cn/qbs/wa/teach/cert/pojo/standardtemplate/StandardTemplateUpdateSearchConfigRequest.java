package cn.qbs.wa.teach.cert.pojo.standardtemplate;


import cn.qbs.wa.teach.cert.pojo.common.CommonConfigDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 证书模板(StandardTemplate)更新证书模板参数
 *
 * @author makejava
 * @since 2022-01-19 11:38:22
 */
@Data
public class StandardTemplateUpdateSearchConfigRequest {

    @ApiModelProperty(value = "")
    private Long id;


    @ApiModelProperty(value = "证书搜索配置")
    private CommonConfigDTO certSearchConfig;



}

