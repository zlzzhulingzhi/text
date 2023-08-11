package cn.qbs.wa.teach.cert.api.pojo.DTO.cert.awardRecord;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/2/14 11:10
 */
@Data
public class CertAwardRequestDTO {

    @ApiModelProperty(value = "证书id")
    @NotNull(message = "证书id不能为空")
    private Long certId;


    @ApiModelProperty(value = "机构id")
    @NotNull(message = "机构id不能为空")
    private Long orgId;

    @ApiModelProperty(value = "来源标记")
    private String sourceMark;

    @ApiModelProperty(value = "来源id")
    private Long sourceId;

    @ApiModelProperty(value = "来源类型 1 证书模块 2任务模块")
    private Integer sourceType;

    @ApiModelProperty(value = "用户列表")
    private List<CertUserResponseDTO> userList;
}
