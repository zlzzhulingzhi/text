package cn.qbs.wa.train.basic.pojo.dict;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/15 14:11
 */
@Data
public class DictChildRequest {

    @ApiModelProperty("父id")
    Long parentId;

    @ApiModelProperty(value = "字典码")
    private String code;
}
