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
 * @since 2022-10-13 13:59:24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WorkflowNode extends Model {

    private static final long serialVersionUID = 264650302872893441L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "流程ID")
    private Long workflowId;

    @ApiModelProperty(value = "节点代号")
    private String nodeCode;

    @ApiModelProperty(value = "节点名称")
    private String nodeName;

    @ApiModelProperty(value = "节点描述")
    private String nodeDesc;

    @ApiModelProperty(value = "主办类型(role-角色 user-用户)")
    private String managerType;

    @ApiModelProperty(value = "主办类型关联对象(角色使用code, 用户使用userId)")
    private String managerRef;

    @ApiModelProperty(value = "排序序号")
    private Integer sort;

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
