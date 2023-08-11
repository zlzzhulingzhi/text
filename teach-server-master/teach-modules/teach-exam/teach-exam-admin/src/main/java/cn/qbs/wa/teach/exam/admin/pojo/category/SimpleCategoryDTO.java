package cn.qbs.wa.teach.exam.admin.pojo.category;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author zcm
 * @Date 2021/12/2 16:16
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleCategoryDTO {

    @ApiModelProperty(value = "分类ID")
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String name;

}
