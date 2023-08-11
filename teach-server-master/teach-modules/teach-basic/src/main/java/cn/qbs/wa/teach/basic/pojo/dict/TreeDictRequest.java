package cn.qbs.wa.teach.basic.pojo.dict;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/10 16:04
 */
@Data
public class TreeDictRequest  {

    @ApiModelProperty(value = "字典码")
    private String code;

}
