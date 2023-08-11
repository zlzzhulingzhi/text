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
 * @since 2021-11-12 09:18:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrganizationMenu extends Model {

    private static final long serialVersionUID = -56863283358516201L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "【应用模块ID】")
    private Long orgId;

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
