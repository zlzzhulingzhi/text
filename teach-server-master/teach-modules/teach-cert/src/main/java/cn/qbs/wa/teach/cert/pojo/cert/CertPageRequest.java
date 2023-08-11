package cn.qbs.wa.teach.cert.pojo.cert;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 证书(Cert)分页查询参数
 *
 * @author makejava
 * @since 2022-01-19 19:19:22
 */
@Data
public class CertPageRequest extends BasePageRequest {


    @ApiModelProperty(value = "证书名称")
    private String name;

    @ApiModelProperty(value = "用户id",hidden = true)
    private Long userId;

    @ApiModelProperty(value = "证书id")
    private Long certId;


}

