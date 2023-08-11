package cn.qbs.wa.teach.question.pojo.category;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author zcm
 * @Date 2021/12/2 16:16
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleCategoryDTO implements Serializable {

    @ApiModelProperty(value = "分类ID")
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String name;

}
