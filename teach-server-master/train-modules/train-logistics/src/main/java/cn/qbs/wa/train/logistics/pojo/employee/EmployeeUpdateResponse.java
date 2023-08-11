package cn.qbs.wa.train.logistics.pojo.employee;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/18 9:11
 */
@Data
public class EmployeeUpdateResponse {

    @ApiModelProperty(value = "【部门ID数组】")
    private List<Long> deptIdList;
}
