package cn.qbs.wa.teach.course.standard.pojo.coursestatisticoverview;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

import java.math.BigDecimal;

/**
 * 【课程统计信息】(CourseStatisticOverview)分页查询参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:39
 */
@Data
public class CourseStatisticOverviewPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【课程评分】= 【课程总评分】/ 【课程评分人数】")
    private BigDecimal score;

    @ApiModelProperty(value = "【课程总评分】")
    private BigDecimal totalScore;

    @ApiModelProperty(value = "【课程评分人数】")
    private Integer evaluatorNum;

    @ApiModelProperty(value = "【报名人数】")
    private Integer signUpNum;

    @ApiModelProperty(value = "【购买人数】")
    private Integer buyerNum;

    @ApiModelProperty(value = "【学习总人数】")
    private Integer studyTotalNum;

    @ApiModelProperty(value = "【总完成人数】")
    private Integer studyCompletedNum;

}

