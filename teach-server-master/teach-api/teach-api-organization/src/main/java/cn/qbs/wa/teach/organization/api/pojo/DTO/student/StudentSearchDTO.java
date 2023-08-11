package cn.qbs.wa.teach.organization.api.pojo.DTO.student;

import cn.qbs.wa.teach.common.core.domain.BasePageSearchComDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author yjx
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StudentSearchDTO extends BasePageSearchComDTO {

    private Long orgId;

    private List<Long> userIds;

    private List<Long> idList;

    private String phone;

    private String realName;

    private Integer sex;

    private Integer enabled;

    /**
     * 内部学员查询标识-1
     */
    private Integer inner;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 部门id
     */
    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "标签id")
    private Long groupId;

    private List<Long> deptIdList;

    private List<Long> groupIdList;

    private List<Long> roleIdList;

    private List<Long> ignoreIdList;

    @ApiModelProperty(value = "身份证号码")
    private String idNumber;

    @ApiModelProperty(value = "【标签名】")
    private String groupName;

    @ApiModelProperty(value = "【部门名】")
    private String deptName;

    @ApiModelProperty(value = "【学员身份 1-普通学员(默认) 2-内部员工")
    private Integer identity;

    @ApiModelProperty(value = "学员排序属性【realName/createTime】")
    private String studentSortField;

    @ApiModelProperty(value = "学员排序正序/倒序【asc/desc】")
    private String studentSortOrder;
}
