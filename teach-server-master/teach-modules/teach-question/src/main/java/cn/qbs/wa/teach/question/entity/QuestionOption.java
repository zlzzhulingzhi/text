package cn.qbs.wa.teach.question.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zcm
 * @since 2021-11-06 15:16:52
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionOption extends Model {

    private static final long serialVersionUID = -80724619591890324L;


    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "试题ID")
    private Long questionId;

    @TableField(value = "`option`")
    @ApiModelProperty(value = "选项[A-F]")
    private String option;

    @ApiModelProperty(value = "选项内容")
    private String content;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

}
