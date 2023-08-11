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
 * @since 2022-10-13 13:56:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WorkflowInstanceAttach extends Model {

    private static final long serialVersionUID = -82287429419375383L;


    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "流程节点实例ID")
    private Long instanceNodeId;

    @ApiModelProperty(value = "流程实例编号")
    private String processNo;

    @ApiModelProperty(value = "附件名称")
    private String attachName;

    @ApiModelProperty(value = "附件地址")
    private String attachUrl;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}
