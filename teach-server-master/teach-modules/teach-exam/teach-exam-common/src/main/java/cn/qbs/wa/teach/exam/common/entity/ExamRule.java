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
 * @author zcm
 * @since 2021-12-13 17:27:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExamRule extends Model {

    private static final long serialVersionUID = 576370990111505549L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "考试id")
    private Long examId;

    @ApiModelProperty(value = "规则id")
    private Long ruleId;

    @ApiModelProperty(value = "规则额外内容")
    private String extraContent;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}
