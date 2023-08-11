package cn.qbs.wa.teach.exam.admin.pojo.exam;


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

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "考试id")
    private Long examId;

    @ApiModelProperty(value = "考试名称")
    private String examName;

    @ApiModelProperty(value = "状态  1 未考试 2 考试中 3考试结束")
    private Integer status;

    @ApiModelProperty(value = "上架状态 0 下架 1上架")
    private Integer shelfStatus;

    @ApiModelProperty(value = "难度")
    private Integer difficulty;

    @ApiModelProperty(value = "学员昵称")
    private String realName;

    @ApiModelProperty(value = "学员id")
    private Long userId;

    @ApiModelProperty(value = "批阅状态 1 未批改  2已批改")
    private Integer correctStatus;

    @ApiModelProperty(value = "时间排序")
    private String timeSort;
}

