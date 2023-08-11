package cn.qbs.wa.teach.organization.api.pojo.DTO.worganization;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 机构插件表(WOrg)创建机构插件表参数
 *
 * @author makejava
 * @since 2022-03-01 13:46:16
 */
@Data
public class WOrgAddDTO {

    @ApiModelProperty(value = "主键id组")
    private List<Long> idList;




}

