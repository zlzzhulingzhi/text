package cn.qbs.wa.teach.common.core.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/10 9:18
 */
@Data
public class BatchFlagRequest extends IdListRequest {

    @ApiModelProperty(value = "0 否 1是")
    Integer flag;

}
