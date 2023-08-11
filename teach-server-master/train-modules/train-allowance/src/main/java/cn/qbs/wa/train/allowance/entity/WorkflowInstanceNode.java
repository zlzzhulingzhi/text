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
public class WorkflowInstanceNode extends Model {

    private static final long serialVersionUID = -31465120932790680L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "流程ID")
    private Long workflowId;

    @ApiModelProperty(value = "流程实例编号")
    private String processNo;

    @ApiModelProperty(value = "节点代号(实例化)")
    private String nodeCode;

    @ApiModelProperty(value = "流程节点(实例化)")
    private String nodeName;

    @ApiModelProperty(value = "主办类型(role-角色 user-用户)")
    private String managerType;

    @ApiModelProperty(value = "主办类型关联对象(角色使用code, 用户使用userId)")
    private String managerRef;

    @ApiModelProperty(value = "上一个流程节点ID")
    private Long prevId;

    @ApiModelProperty(value = "下一个流程节点ID")
    private Long nextId;

    @ApiModelProperty(value = "状态(10-未开始 11-进行中 21-通过 22-未通过)")
    private Integer status;

    @ApiModelProperty(value = "审批意见")
    private String comment;

    @ApiModelProperty(value = "审批人ID")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "审批时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
