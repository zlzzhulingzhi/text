package cn.qbs.wa.teach.organization.api.pojo.DTO.worganization;

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
public class WOrganizationPageSearchDTO extends BasePageSearchComDTO {

    @ApiModelProperty(value = "机构名称")
    private String name;


    @ApiModelProperty(value = "1: 企业 2: 高校 4: 培训机构")
    private Integer category;

}
