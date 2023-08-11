package cn.qbs.wa.teach.basic.pojo.menu;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/4 18:17
 */
@Data
public class TreeMenuTotalRequest {

    @ApiModelProperty(value = "菜单类别",example = "admin(后台管理),door(门户)")
    @NotBlank(message = "参数不能为空")
    private String category;
}
