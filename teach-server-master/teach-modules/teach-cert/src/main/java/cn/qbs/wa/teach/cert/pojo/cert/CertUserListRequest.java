package cn.qbs.wa.teach.cert.pojo.cert;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CertUserListRequest {

    @ApiModelProperty(value = "部门id")
    private Long deptId;
}
