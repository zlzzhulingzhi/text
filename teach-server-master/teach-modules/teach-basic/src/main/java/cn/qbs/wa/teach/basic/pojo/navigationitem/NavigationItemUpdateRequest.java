package cn.qbs.wa.teach.basic.pojo.navigationitem;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 【导航栏项】(NavigationItem)更新【导航栏项】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-08 13:55:36
 */
@Data
public class NavigationItemUpdateRequest {
    
    @ApiModelProperty(value = "【主键标识】")
    private Long id;
    
    @ApiModelProperty(value = "【父导航ID】")
    private Long parentId;
    
    @ApiModelProperty(value = "【导航名称】")
    private String name;
    
    @ApiModelProperty(value = "【导航编码】")
    private String code;
    
    @ApiModelProperty(value = "【导航类型 1-一级导航 2-二级导航】")
    private Integer type;
    
    @ApiModelProperty(value = "【导航URI】")
    private String uri;
    
    @ApiModelProperty(value = "【导航权限 例：nav:course:list】")
    private String permission;
    
    @ApiModelProperty(value = "【导航排序】")
    private Integer sort;
    
    @ApiModelProperty(value = "【启用状态 false-0-未启用 true-1-启用】")
    private Integer enabled;

}

