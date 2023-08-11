package cn.qbs.wa.teach.organization.pojo.employee;

import cn.qbs.wa.teach.organization.pojo.importrecord.ImportCountResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/1/14 11:03
 */
@Data
public class EmployeeBatchAddResponse extends ImportCountResponse {

    @ApiModelProperty(value = "部门数组")
    List<Long> deptIdList;
}
