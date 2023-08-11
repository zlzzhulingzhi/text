package cn.qbs.wa.teach.exam.answer.pojo.center;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 考试表(Exam)分页查询参数
 *
 * @author zcm
 * @since 2021-12-14 13:51:56
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageRequest extends BasePageRequest {

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "考试名称")
    private String examName;

    @ApiModelProperty(value = "状态 1-未开始 2-考试中 3-考试结束")
    private Integer status;

    @ApiModelProperty(value = "难度")
    private Integer difficulty;

    @ApiModelProperty(value = "是否完成 是否完成 0 进行中 1 已完成 2已批改 3作弊 4 未开始")
    private Integer complete;

    @ApiModelProperty(value = "用户id", hidden = true)
    private Long userId;

}

