package cn.qbs.wa.teach.question.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author zcm
 * @since 2021-11-03 18:13:59
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionType extends Model {

    private static final long serialVersionUID = 104860157332493881L;


    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "题型名称")
    @NotBlank(message = "题型名称不能为空！")
    private String name;

    @ApiModelProperty(value = "创建者ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "创建日期")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后更新者ID")
    private Long updateBy;

    @ApiModelProperty(value = "最后更新日期")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "排序号")
    private Integer sortNum;

    @ApiModelProperty(value = "是否启用 【1：启用，0：废弃】")
    private Boolean enabled;

}
