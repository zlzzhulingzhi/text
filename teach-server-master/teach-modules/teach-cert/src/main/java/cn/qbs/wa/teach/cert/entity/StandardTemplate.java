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
 * @since 2022-01-19 17:22:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StandardTemplate extends Model {

    private static final long serialVersionUID = -55676476045190963L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "证书模板名称")
    private String name;

    @ApiModelProperty(value = "0 禁用 1正常")
    private Integer enabled;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "背景图片地址")
    private String backgroundUrl;

    @ApiModelProperty(value = "模式 1 2")
    private Integer modelType;

}
