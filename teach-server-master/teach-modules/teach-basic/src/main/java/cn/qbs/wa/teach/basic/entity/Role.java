package cn.qbs.wa.teach.basic.entity;


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
 * @since 2021-11-12 09:25:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Role extends Model {

    private static final long serialVersionUID = -17356281513505942L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "【角色代码】")
    private String code;

    @ApiModelProperty(value = "【角色名称】")
    private String name;

    @ApiModelProperty(value = "【角色备注】")
    private String remark;

    @ApiModelProperty(value = "【角色排序序号】")
    private Integer sort;

    @ApiModelProperty(value = "【启用状态 false-0-未启用 true-1-启用】")
    private Integer enabled;

    @ApiModelProperty(value = "权重")
    private Integer priority;

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

    @ApiModelProperty(value = "【应用模块ID】 预留")
    private Long appId;

}
