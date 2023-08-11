package cn.qbs.wa.teach.organization.pojo.worg;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 机构插件表(WOrg)分页查询参数
 *
 * @author makejava
 * @since 2022-03-01 13:46:16
 */
@Data
public class WOrgPageRequest extends BasePageRequest {


    @ApiModelProperty(value = "机构名称")
    private String name;


    @ApiModelProperty(value = "1: 企业 2: 高校 4: 培训机构")
    private Integer category;
}

