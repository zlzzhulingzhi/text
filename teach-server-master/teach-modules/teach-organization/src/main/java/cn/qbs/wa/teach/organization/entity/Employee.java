package cn.qbs.wa.teach.organization.entity;


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
 * @since 2021-11-15 09:07:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Employee extends Model {

    private static final long serialVersionUID = 790635034806140849L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【手机号码】")
    private String phone;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.UPDATE)
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

}
