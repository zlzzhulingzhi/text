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
 * @since 2021-11-12 09:18:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Organization extends Model {

    private static final long serialVersionUID = 281819583908654846L;


    @ApiModelProperty(value = "标识")
    private Long id;

    @ApiModelProperty(value = "机构名称")
    private String name;

    @ApiModelProperty(value = "排序号")
    private Integer sort;

    @ApiModelProperty(value = "启用状态 0-禁用 1-启用")
    private Integer enabled;

    @ApiModelProperty(value = "最后修改人ID")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "最后修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "机构性质")
    private String category;

    @ApiModelProperty(value = "PC端logo url")
    private String pcLogoUrl;

    @ApiModelProperty(value = "H5端logo url")
    private String h5LogoUrl;

    @ApiModelProperty(value = "域名")
    private String domain;

    @ApiModelProperty(value = "机构介绍")
    private String intro;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

}
