package cn.qbs.wa.teach.question.entity;


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
 * @since 2021-11-09 10:20:57
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionCategory extends Model {

    private static final long serialVersionUID = -22786511991515620L;


    @ApiModelProperty(value = "ID")
    private Integer id;

    @ApiModelProperty(value = "创建者ID")
    private Long createBy;

    @ApiModelProperty(value = "创建日期")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "试题ID")
    private Long questionId;

    @ApiModelProperty(value = "分类ID")
    private Long categoryId;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

}
