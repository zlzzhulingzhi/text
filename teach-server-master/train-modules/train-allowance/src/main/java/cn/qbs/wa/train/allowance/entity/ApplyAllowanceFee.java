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
 * @since 2022-11-03 19:43:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApplyAllowanceFee extends Model {

    private static final long serialVersionUID = -70877133441781246L;


    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "资助资金申请ID")
    private Long applyAllowanceId;

    @ApiModelProperty(value = "资助项目编号")
    private String itemCode;

    @ApiModelProperty(value = "资助项目名称")
    private String itemName;

    @ApiModelProperty(value = "资助费用(元)")
    private BigDecimal allowanceFee;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人ID")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
