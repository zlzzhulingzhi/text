package cn.qbs.wa.teach.cert.pojo.standardtemplate;

import cn.qbs.wa.teach.cert.pojo.common.CommonConfigDTO;
import cn.qbs.wa.teach.cert.pojo.common.CommonConfigDetailDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 证书模板(StandardTemplate)证书模板详情
 *
 * @author makejava
 * @since 2022-01-19 11:38:22
 */
@Data
public class StandardTemplateDetailSearchConfigResponse {

    @ApiModelProperty(value = "证书搜索配置")
    private CommonConfigDTO certSearchConfig;



}

