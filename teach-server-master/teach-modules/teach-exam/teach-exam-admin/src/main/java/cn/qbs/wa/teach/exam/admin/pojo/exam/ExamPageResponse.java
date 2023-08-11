package cn.qbs.wa.teach.exam.admin.pojo.exam;

import cn.qbs.wa.teach.exam.common.entity.Exam;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 考试表(Exam)分页查询考试表响应
 *
 * @author zcm
 * @since 2021-12-14 13:52:10
 */
@Data
public class ExamPageResponse extends Exam {

    @ApiModelProperty(value = "考生数")
    private Integer examineeCount;

    @ApiModelProperty(value = "批阅状态")
    private Integer status;

    @ApiModelProperty(value = "批改数")
    private Integer correctCount;

    @ApiModelProperty(value = "提交数")
    private Integer submitCount;

    @ApiModelProperty(value = "平均分")
    private BigDecimal avgScore;

    @ApiModelProperty(value = "最高分")
    private BigDecimal highestScore;

    @ApiModelProperty(value = "最低分")
    private BigDecimal lowestScore;

    @ApiModelProperty(value = "创建者用户ID")
    private Long createBy;

    @ApiModelProperty(value = "创建者姓名")
    private String createByName;

    @ApiModelProperty(value = "学员昵称")
    private String realName;

    @ApiModelProperty(value = "学员id")
    private Long userId;

    @ApiModelProperty(value = "得分")
    private Long score;

    @ApiModelProperty(value = "提交时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime submitTime;

    @ApiModelProperty(value = "规则列表")
    private List<ExamRuleDTO> ruleList;

}

