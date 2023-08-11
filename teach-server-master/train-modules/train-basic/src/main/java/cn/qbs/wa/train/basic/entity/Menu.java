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
 * @since 2021-11-12 09:25:22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Menu extends Model {

    private static final long serialVersionUID = -76433149256002046L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "【应用模块ID】")
    private Long appId;

    @ApiModelProperty(value = "【上级菜单ID】")
    private Long parentId;

    @ApiModelProperty(value = "【菜单类型 0-应用 1-菜单 2-板块 3-按钮】")
    private Integer type;

    @ApiModelProperty(value = "【菜单URI】")
    private String uri;

    @ApiModelProperty(value = "【菜单权限 例：student:score:more】")
    private String permission;

    @ApiModelProperty(value = "【菜单名称】")
    private String name;

    @ApiModelProperty(value = "【菜单图标URL】")
    private String iconUrl;

    @ApiModelProperty(value = "【菜单类别】 预留")
    private String category;

    @ApiModelProperty(value = "【菜单排序】")
    private Integer sort;

    @ApiModelProperty(value = "【启用状态 false-0-未启用 true-1-启用】")
    private Integer enabled;

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

    @ApiModelProperty(value = "【菜单说明】")
    private String remark;

}
