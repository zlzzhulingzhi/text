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
 * @author zcm
 * @since 2021-12-13 17:27:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExamInfo extends Model {

    private static final long serialVersionUID = 714805683276953858L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "考试id")
    private Long examId;

    @ApiModelProperty(value = "最高分")
    private BigDecimal highestScore;

    @ApiModelProperty(value = "最低分")
    private BigDecimal lowestScore;

    @ApiModelProperty(value = "平均分")
    private BigDecimal averageScore;

    @ApiModelProperty(value = "可见用户状态 1 所有人可见 2 部分可见")
    private Integer userVisibleStatus;

    @ApiModelProperty(value = "考生人数")
    private Integer examineeCount;

    @ApiModelProperty(value = "发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime releaseTime;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

}
