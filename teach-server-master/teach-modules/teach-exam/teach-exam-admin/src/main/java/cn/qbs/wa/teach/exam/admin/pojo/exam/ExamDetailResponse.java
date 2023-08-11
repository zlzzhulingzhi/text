package cn.qbs.wa.teach.exam.admin.pojo.exam;

import cn.qbs.wa.teach.exam.common.entity.Exam;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
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

    @ApiModelProperty(value = "规则ID列表")
    private List<Long> ruleIdList;

    @ApiModelProperty(value = "可见用户列表")
    private List<VisibleUser> visibleUserList;

    @ApiModelProperty(value = "试卷名称")
    private String paperName;

    @ApiModelProperty(value = "发布时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime releaseTime;

    @ApiModelProperty(value = "规则配置列表")
    List<ExamRuleConfigDTO> ruleList;

}

