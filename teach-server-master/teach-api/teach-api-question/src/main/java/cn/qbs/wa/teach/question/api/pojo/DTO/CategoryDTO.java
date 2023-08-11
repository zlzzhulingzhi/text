package cn.qbs.wa.teach.question.api.pojo.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zcm
 * @Date 2021/11/26 16:56
 * @Version 1.0
 */
@Data
public class CategoryDTO {

    @ApiModelProperty(value = "分类ID")
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String name;

}
