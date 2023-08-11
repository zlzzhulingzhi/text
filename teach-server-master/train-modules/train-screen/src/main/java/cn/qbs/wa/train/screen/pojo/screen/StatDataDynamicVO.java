package cn.qbs.wa.train.screen.pojo.screen;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author makejava
 * @since 2022-10-09 17:04:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class StatDataDynamicVO implements Serializable {

    @ApiModelProperty(value = "数据名称")
    private String dataName;

    @ApiModelProperty(value = "已使用数量")
    private Integer using;

    @ApiModelProperty(value = "空闲数量")
    private Integer free;

}
