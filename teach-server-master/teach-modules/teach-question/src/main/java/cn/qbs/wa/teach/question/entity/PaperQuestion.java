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
 * @since 2021-11-19 15:18:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PaperQuestion extends Model {

    private static final long serialVersionUID = -37838126293879693L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "试卷ID")
    private Long paperId;

    @ApiModelProperty(value = "试题ID")
    private Long questionId;

    @ApiModelProperty(value = "该题在该试卷中的分值")
    private BigDecimal score;

    @ApiModelProperty(value = "父题ID")
    private Long parentId;

    @ApiModelProperty(value = "题目序号(试卷内不分题型)")
    private Integer globalOrder;

    @ApiModelProperty(value = "排序号(同一题型内)")
    private Integer localOrder;

    @ApiModelProperty(value = "显示高度")
    private Integer displayHeight;

    @ApiModelProperty(value = "创建者ID")
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

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

}
