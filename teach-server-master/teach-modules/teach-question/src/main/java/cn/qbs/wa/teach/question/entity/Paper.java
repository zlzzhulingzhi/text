package cn.qbs.wa.teach.question.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author zcm
 * @since 2021-11-18 20:48:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Paper extends Model {

    private static final long serialVersionUID = -61847703127485529L;


    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "试卷名称")
    private String name;

    @ApiModelProperty(value = "试卷题目总数")
    private Integer questionCount;

    @ApiModelProperty(value = "试卷总分")
    private BigDecimal totalScore;

    @ApiModelProperty(value = "是否启用 【1：启用，0：废弃】")
    private Boolean enabled;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "创建者ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "创建日期")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后更新者ID")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "最后更新日期")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "编辑状态 0 不可编辑 1可编辑")
    private Boolean editable;

}
