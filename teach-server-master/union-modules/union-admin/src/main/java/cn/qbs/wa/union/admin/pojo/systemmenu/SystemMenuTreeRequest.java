package cn.qbs.wa.union.admin.pojo.systemmenu;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 【课程分类】(Category)列表查询参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:36
 */
@Data
public class SystemMenuTreeRequest {


    @ApiModelProperty(value = "【上级分类ID】")
    private Long parentId;

    @ApiModelProperty(value = "【是否启用】")
    private Integer enabled;



}

