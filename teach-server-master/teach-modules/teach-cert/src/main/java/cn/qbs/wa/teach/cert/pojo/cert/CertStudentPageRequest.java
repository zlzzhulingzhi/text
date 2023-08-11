package cn.qbs.wa.teach.cert.pojo.cert;

import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentSearchDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yjx
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CertStudentPageRequest extends StudentSearchDTO {

    @ApiModelProperty(value = "证书id")
    private Long certId;

    @ApiModelProperty(value = "身份证号码")
    private String idNum;
}
