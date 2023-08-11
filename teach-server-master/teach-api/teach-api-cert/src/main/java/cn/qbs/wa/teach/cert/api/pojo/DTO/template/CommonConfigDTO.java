package cn.qbs.wa.teach.cert.api.pojo.DTO.template;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/1/19 13:46
 */
@Data
public class CommonConfigDTO {

    @ApiModelProperty(value = "配置列表")
    List<CommonConfigDetailDTO> configList;


}
