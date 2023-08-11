package cn.qbs.wa.teach.organization.api.pojo.DTO.employee;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/18 10:20
 */
@Data
public class EmployeeListSearchDTO {

    @ApiModelProperty("机构id")
    Long orgId;

    @ApiModelProperty("搜索内容")
    String searchContent;

    @ApiModelProperty("职工id数组")
    List<Long> idList;

    @ApiModelProperty("用户id数组")
    List<Long> userIdList;
}
