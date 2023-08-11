package cn.qbs.wa.teach.basic.pojo.dict;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.teach.basic.entity.Dict;

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

