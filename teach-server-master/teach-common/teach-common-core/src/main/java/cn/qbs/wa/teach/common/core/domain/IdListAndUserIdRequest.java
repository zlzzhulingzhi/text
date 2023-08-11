package cn.qbs.wa.teach.common.core.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/10 8:29
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IdListAndUserIdRequest extends IdListRequest {

    @ApiModelProperty(value = "执行者id")
    Long userId;
}
