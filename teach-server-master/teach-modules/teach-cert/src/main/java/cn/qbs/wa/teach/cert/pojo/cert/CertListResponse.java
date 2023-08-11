package cn.qbs.wa.teach.cert.pojo.cert;


import cn.qbs.wa.teach.cert.pojo.common.CommonConfigDetailLiteDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 证书(Cert)分页查询参数
 *
 * @author makejava
 * @since 2022-01-19 19:19:22
 */
@Data
public class CertListResponse {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "证书名称")
    private String name;

    @ApiModelProperty(value = "查询配置列表")
    List<CommonConfigDetailLiteDTO> searchConfigList;

}

