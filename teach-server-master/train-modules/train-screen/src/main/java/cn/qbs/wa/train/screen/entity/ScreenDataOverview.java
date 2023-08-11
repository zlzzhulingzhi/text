package cn.qbs.wa.train.screen.entity;


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
 * @since 2022-10-09 17:04:41
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ScreenDataOverview extends Model {

    private static final long serialVersionUID = -51993110978638036L;


    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "字段代号")
    private String fieldCode;

    @ApiModelProperty(value = "显示名称")
    private String displayName;

    @ApiModelProperty(value = "显示值")
    private String displayValue;

    @ApiModelProperty(value = "显示图标")
    private String displayIcon;

    @ApiModelProperty(value = "排序号")
    private Integer sort;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
