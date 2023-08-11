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
 * @since 2022-01-18 09:48:39
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Category extends Model {

    private static final long serialVersionUID = -43243055152777838L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "平台类型 1 内训 2外训")
    private Integer platformType;

    @ApiModelProperty(value = "上级分类id")
    private Long parentId;

    @ApiModelProperty(value = "分组类型 1 新闻")
    private Integer groupType;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "分类代号")
    private String categoryCode;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "启用状态 0-未启用 1-已启用")
    private Integer enabled;

    @ApiModelProperty(value = "删除状态 0 正常 1删除")
    private Integer deleted;

    @ApiModelProperty(value = "编辑状态 0不能编辑  1能编辑")
    private Integer edited;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "是否为模板 0-否 1-是")
    private Integer template;

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

}
