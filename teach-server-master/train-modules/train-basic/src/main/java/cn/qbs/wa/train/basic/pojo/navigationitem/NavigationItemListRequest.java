package cn.qbs.wa.train.basic.pojo.navigationitem;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 【导航栏项】(NavigationItem)查询参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-08 13:55:36
 */
@Data
public class NavigationItemListRequest {
    
    @ApiModelProperty(value = "【父导航ID】")
    private Long parentId;
    
    @ApiModelProperty(value = "【导航编码】")
    private String code;
    
    @ApiModelProperty(value = "【导航类型 1-一级导航 2-二级导航】")
    private Integer type;

    @ApiModelProperty(value = "【启用状态 false-0-未启用 true-1-启用】")
    private Integer enabled;

}

