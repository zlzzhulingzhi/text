package cn.qbs.wa.train.allowance.entity;


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
 * @author makejava
 * @since 2023-04-04 13:35:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApplyAuditExpert extends Model {

    private static final long serialVersionUID = 412058002983978894L;


    @ApiModelProperty(value = "主键")
    private  Long id;

    @ApiModelProperty(value = "申请ID")
    private Long applyId;

    @ApiModelProperty(value = "申请类型(申请类型(qualification-资助资格 allowance-资助资金)")
    private String applyType;

    @ApiModelProperty(value = "专家ID")
    private Long expertId;

    @ApiModelProperty(value = "评审费用(单位元)")
    private BigDecimal cost;

    @ApiModelProperty(value = "评审意见")
    private String comment;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}
