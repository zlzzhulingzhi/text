package cn.qbs.wa.teach.cert.pojo.cert;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 证书(Cert)分页查询参数
 *
 * @author makejava
 * @since 2022-01-19 19:19:22
 */
@Data
public class CertListRequest  {


    @ApiModelProperty(value = "证书名称")
    private String name;



}

