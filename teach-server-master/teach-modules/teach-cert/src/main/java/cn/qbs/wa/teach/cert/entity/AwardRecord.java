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
 * @since 2022-04-11 13:38:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AwardRecord extends Model {

    private static final long serialVersionUID = 363989281503505349L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构Id")
    private Long orgId;

    @ApiModelProperty(value = "证书Id")
    private Long certId;

    @ApiModelProperty(value = "用户Id")
    private Long userId;

    @ApiModelProperty(value = "学员Id")
    private Long studentId;

    @ApiModelProperty(value = "证书前缀规则")
    private String certPrefix;

    @ApiModelProperty(value = "证书编号")
    private String certNum;

    @ApiModelProperty(value = "禁用0 启用1")
    private Integer enabled;

    @ApiModelProperty(value = "证书图片路径")
    private String certImageUrl;

    @ApiModelProperty(value = "来源标记")
    private String sourceMark;

    @ApiModelProperty(value = "来源id")
    private Long sourceId;

    @ApiModelProperty(value = "来源类型 1 证书模块 2任务模块")
    private Integer sourceType;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

}
