package cn.qbs.wa.teach.cert.pojo.cert;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CertExamUserListRequest {

    @NotNull(message = "考试ID不能为空！")
    @ApiModelProperty(value = "考试id")
    private Long examId;

    @ApiModelProperty(value = "1 查询通过 2查询未通过")
    private Integer searchStatus;

    @ApiModelProperty(value = "证书id")
    private Long certId;
}
