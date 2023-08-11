package cn.qbs.wa.train.allowance.entity;

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
 * @since 2022-11-02 18:59:29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApplyAttach extends Model {

    private static final long serialVersionUID = -97025980893520199L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "申请ID")
    private Long applyId;

    @ApiModelProperty(value = "申请类型(qualification-资助资格 allowance-资助资金)")
    private String applyType;

    @ApiModelProperty(value = "数据板块(apply-申请板块 audit-审核板块)")
    private String section;

    @ApiModelProperty(value = "附件代号")
    private String attachCode;

    @ApiModelProperty(value = "附件名称")
    private String attachName;

    @ApiModelProperty(value = "附件地址")
    private String attachUrl;

    @ApiModelProperty(value = "排序号")
    private Integer sort;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}
