package cn.qbs.wa.teach.exam.answer.pojo.exam;

import cn.qbs.wa.teach.exam.common.entity.Exam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 考试表(Exam)考试表详情
 *
 * @author zcm
 * @since 2021-12-14 13:52:10
 */
@Data
public class ExamDetailResponse extends Exam {

    @ApiModelProperty(value = "考试分类ID列表")
    private List<Long> categoryIdList;

    @ApiModelProperty(value = "打乱试题顺序")
    private Boolean shuffleQuestion = false;

    @ApiModelProperty(value = "显示答案解析")
    private Boolean showAnswerDesc = false;

    @ApiModelProperty(value = "规则ID列表")
    private List<Long> ruleIdList;

    @ApiModelProperty(value = "可见用户列表")
    private List<VisibleUser> visibleUserList;

}

