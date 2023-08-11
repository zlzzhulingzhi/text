package cn.qbs.wa.teach.exam.common.entity;


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
 * 考生成绩
 * @author zcm
 * @since 2022-05-24 16:23:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExamineeScore extends Model {

    private static final long serialVersionUID = 560294856259490886L;


    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 机构ID
     */
    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    /**
     * 考生ID
     */
    @ApiModelProperty(value = "考生ID")
    private Long examineeId;

    /**
     * 考试记录ID
     */
    @ApiModelProperty(value = "考试记录ID")
    private Long examineeRecordId;

    /**
     * 分数
     */
    @ApiModelProperty(value = "分数")
    private BigDecimal score;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Long createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private Long updateBy;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
