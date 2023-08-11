package cn.qbs.wa.teach.question.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author zcm
 * @Date 2021/11/9 10:53
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectOption implements Serializable {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "难度名称")
    private String name;

    @ApiModelProperty(value = "排序号")
    private Integer sortNum;

}
