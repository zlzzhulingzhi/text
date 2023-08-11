package cn.qbs.wa.train.logistics.pojo.worg;


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
public class WOrgAddRequest {

    @ApiModelProperty(value = "主键id组")
    private List<Long> idList;

}

