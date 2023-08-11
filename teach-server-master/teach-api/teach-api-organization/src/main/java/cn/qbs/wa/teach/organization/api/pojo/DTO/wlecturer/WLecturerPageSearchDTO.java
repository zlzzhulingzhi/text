package cn.qbs.wa.teach.organization.api.pojo.DTO.wlecturer;

import cn.qbs.wa.teach.common.core.domain.BasePageSearchComDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/3/1 14:01
 */
@Data
public class WLecturerPageSearchDTO extends BasePageSearchComDTO {

    @ApiModelProperty(value = "讲师名称")
    String name;

    @ApiModelProperty(value = "0 禁用 1启用")
    private Integer enabled;

}
