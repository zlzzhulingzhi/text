package cn.qbs.wa.teach.cert.entity;


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
 * @since 2022-02-17 08:44:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StandardTemplateConfig extends Model {

    private static final long serialVersionUID = -57476193380593978L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "模板id")
    private Long templateId;

    @ApiModelProperty(value = "配置类型 1.证书参数配置 2.证书查询配置")
    private Integer type;

    @ApiModelProperty(value = "配置码")
    private String code;

    @ApiModelProperty(value = "配置名称")
    private String keyName;

    @ApiModelProperty(value = "配置值")
    private String keyValue;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "自定义样式")
    private String customStyle;

    @ApiModelProperty(value = "0 禁用 1启用")
    private Integer enabled;

}
