package cn.qbs.wa.train.logistics.pojo.dept;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/17 9:44
 */
@Data
public class DeptChildRequest {
    @ApiModelProperty("父id")
    Long parentId;
}
