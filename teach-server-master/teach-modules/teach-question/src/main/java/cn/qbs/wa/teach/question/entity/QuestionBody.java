package cn.qbs.wa.teach.question.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zcm
 * @since 2021-11-04 10:12:52
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionBody extends Model {

    private static final long serialVersionUID = -72829320291225241L;


    @ApiModelProperty(value = "id 不自增, 与 question 表保持一致")
    private Long id;

    @ApiModelProperty(value = "题干")
    private String content;

    @ApiModelProperty(value = "答案")
    private String answer;

    @ApiModelProperty(value = "答案解析")
    private String answerDesc;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

}
