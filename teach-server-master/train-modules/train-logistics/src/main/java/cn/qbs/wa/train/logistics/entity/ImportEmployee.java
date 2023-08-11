package cn.qbs.wa.train.logistics.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author makejava
 * @since 2021-11-26 16:47:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ImportEmployee extends Model {

    private static final long serialVersionUID = -94593540643699713L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "事件id")
    private String eventId;

    @ApiModelProperty(value = "机构Id")
    private Long orgId;

    @ApiModelProperty(value = "")
    private Long deptId;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "性别 0-未知 1-男 2-女")
    private Integer sex;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "")
    private String idNumber;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}
