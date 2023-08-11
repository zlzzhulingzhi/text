package cn.qbs.wa.teach.cert.api.pojo.DTO.cert;


import cn.qbs.wa.teach.common.core.domain.BasePageSearchComDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 证书(Cert)分页查询参数
 *
 * @author makejava
 * @since 2022-01-19 19:19:22
 */
@Data
public class CertPageRequestDTO extends BasePageSearchComDTO {


    @ApiModelProperty(value = "证书名称")
    private String name;

    @ApiModelProperty(value = "用户id",hidden = true)
    private Long userId;

    @ApiModelProperty(value = "证书id")
    private Long certId;


}

