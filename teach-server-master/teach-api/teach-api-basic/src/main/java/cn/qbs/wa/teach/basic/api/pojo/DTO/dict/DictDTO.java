package cn.qbs.wa.teach.basic.api.pojo.DTO.dict;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/11 14:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DictDTO {

    @ApiModelProperty(value = "字典码")
    private String code;
}
