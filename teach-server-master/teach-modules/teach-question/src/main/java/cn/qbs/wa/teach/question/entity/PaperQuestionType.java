package cn.qbs.wa.teach.question.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author zcm
 * @since 2021-11-19 15:18:23
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PaperQuestionType extends Model {

    private static final long serialVersionUID = 825982716388517245L;


    @ApiModelProperty(value = "ID")
    private Integer id;

    @ApiModelProperty(value = "试卷ID")
    private Long paperId;

    @ApiModelProperty(value = "题型ID")
    private Long questionTypeId;

    @ApiModelProperty(value = "排序号")
    private Integer sortNum;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    public PaperQuestionType(Long paperId, Long questionTypeId, Integer sortNum) {
        this.paperId = paperId;
        this.questionTypeId = questionTypeId;
        this.sortNum = sortNum;
    }

}
