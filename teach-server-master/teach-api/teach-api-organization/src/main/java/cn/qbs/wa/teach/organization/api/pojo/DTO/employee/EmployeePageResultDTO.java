package cn.qbs.wa.teach.organization.api.pojo.DTO.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 职工(Employee)分页查询职工响应
 *
 * @author makejava
 * @since 2021-11-09 20:11:22
 */
@Data
public class EmployeePageResultDTO {

   @ApiModelProperty(value = "")
   private Long id;

   @ApiModelProperty(value = "【真实姓名】")
   private String realName;

   @ApiModelProperty(value = "【账号】")
   private String account;

   @ApiModelProperty(value = "【手机号码】")
   private String phone;

   @ApiModelProperty(value = "")
   private Long createBy;

   @ApiModelProperty(value = "")
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
   private LocalDateTime createTime;

   @ApiModelProperty(value = "")
   private Long updateBy;

   @ApiModelProperty(value = "")
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
   private LocalDateTime updateTime;

   @ApiModelProperty(value = "【组织机构ID】")
   private Long orgId;

   @ApiModelProperty(value = "【系统用户ID】")
   private Long userId;

   @ApiModelProperty(value = "性别 0-未知 1-男 2-女")
   private Integer sex;

   @ApiModelProperty(value = "0表示账号不可用  1表示账号可用")
   private Integer enabled;

   @ApiModelProperty(value = "部门")
   String deptNames;

   @ApiModelProperty(value = "角色")
   String roleNames;

}

