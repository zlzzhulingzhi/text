package cn.qbs.wa.train.basic.pojo.dict;

import cn.qbs.wa.train.basic.entity.Dict;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 字典表(Dict)分页查询字典表响应
 *
 * @author makejava
 * @since 2021-11-08 13:32:07
 */
@Data
public class DictPageResponse extends Dict {

    @ApiModelProperty(value = "是否有子 0 否 1是")
    Integer hasChildren;

}

