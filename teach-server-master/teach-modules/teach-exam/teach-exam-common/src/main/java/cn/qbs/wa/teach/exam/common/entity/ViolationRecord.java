package cn.qbs.wa.teach.exam.common.entity;

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
 * @since 2021-12-27 08:39:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ViolationRecord extends Model {

    private static final long serialVersionUID = 966216737780497928L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "规则id")
    private Long ruleId;

    @ApiModelProperty(value = "内容")
    private String text;

    @ApiModelProperty(value = "职工id")
    private Long employeeId;

    @ApiModelProperty(value = "考生id")
    private Long examineeId;

    @ApiModelProperty(value = "考试记录id")
    private Long examineeRecordId;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

}
