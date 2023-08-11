package cn.qbs.wa.teach.organization.pojo.employee;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 职工(Employee)分页查询参数
 *
 * @author makejava
 * @since 2021-11-09 20:11:21
 */
@Data
public class EmployeeListRequest  {

   @ApiModelProperty("机构id")
   Long orgId;

   @ApiModelProperty("搜索内容")
   String searchContent;

   @ApiModelProperty("姓名")
   String realName;

   @ApiModelProperty("手机号")
   String phone;

   @ApiModelProperty("角色名称")
   String roleName;

   @ApiModelProperty("启/禁用")
   Integer enable;

   @ApiModelProperty("职工id数组")
   List<Long> idList;

   @ApiModelProperty("用户id数组")
   List<Long> userIdList;

}

