package cn.qbs.wa.teach.question.pojo.question.search;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 计数结果
 * @Author zcm
 * @Date 2021/11/17 10:12
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountResult {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "排序号")
    private Integer sortNum;

    @ApiModelProperty(value = "数量")
    private Long quantity;

    public CountResult(Long id, Long quantity) {
        this.id = id;
        this.quantity = quantity;
    }

}
