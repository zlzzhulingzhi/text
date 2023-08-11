package cn.qbs.wa.teach.exam.answer.pojo.exam;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 考试表(Exam)分页查询参数
 *
 * @author zcm
 * @since 2021-12-14 13:51:56
 */
@Data
public class ExamPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "试卷id")
    private Long examId;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "考试名称")
    private String examName;

    @ApiModelProperty(value = "状态  1 未考试 2 考试中 3考试结束")
    private Integer status;

    @ApiModelProperty(value = "上架状态 0 下架 1上架")
    private Integer shelfStatus;

    @ApiModelProperty(value = "难度")
    private Integer difficulty;

    @ApiModelProperty(value = "是否完成 是否完成 0 进行中 1 已完成 2已批改 3作弊 4 未开始")
    private Integer complete;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "考试记录表的id")
    private Long examineeRecordId;

}

