package cn.qbs.wa.teach.common.core.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 *
 *
 * @Author zcm
 * @Date 2022-06-13 16:08
 * @Version 1.0
 */
@Data
public class IdListParam {

    @ApiModelProperty(value = "id列表")
    private List<Long> idList;

}
