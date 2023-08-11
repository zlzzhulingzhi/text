package cn.qbs.wa.teach.cert.api.pojo.DTO.cert;

import cn.qbs.wa.teach.cert.api.pojo.DTO.template.CommonConfigDetailLiteDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/1/22 15:51
 */
@Data
public class MyCertSearchRequestDTO {


    @ApiModelProperty(value = "证书id")
    private Long id;

    @ApiModelProperty(value = "查询配置列表")
    @NotEmpty(message = "查询配置不能为空")
    List<CommonConfigDetailLiteDTO> searchConfigList;
}
