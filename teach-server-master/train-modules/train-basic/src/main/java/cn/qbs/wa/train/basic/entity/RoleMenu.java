package cn.qbs.wa.train.basic.entity;


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
 * @since 2021-11-15 13:59:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleMenu extends Model {

    private static final long serialVersionUID = 712861732646304754L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "【应用模块ID】")
    private Long orgId;

    @ApiModelProperty(value = "【角色ID】")
    private Long roleId;

    @ApiModelProperty(value = "【菜单ID】")
    private Long menuId;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}
