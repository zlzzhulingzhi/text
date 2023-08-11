package cn.qbs.wa.teach.auth.pojo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yjx
 */
@Data
public class CosCredentialTypeRequest {

    @ApiModelProperty(value = "授权类型", notes = "simple(简单上传)/multipart(分块上传)", example = "simple")
    private String type;
}
