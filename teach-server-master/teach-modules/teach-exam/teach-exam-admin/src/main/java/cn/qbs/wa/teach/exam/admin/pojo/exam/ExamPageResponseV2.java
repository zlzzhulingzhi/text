package cn.qbs.wa.teach.exam.admin.pojo.exam;

import cn.qbs.wa.teach.exam.common.entity.Exam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页查询考试响应
 *
 * @Author zcm
 * @Date 2022-05-20 14:00
 * @Version 1.0
 */
@Data
public class ExamPageResponseV2 extends Exam {

    @ApiModelProperty(value = "试卷名称")
    private String paperName;

    @ApiModelProperty(value = "考生数")
    private Integer examineeCount;

    @ApiModelProperty(value = "考试状态")
    private Integer status;

    @ApiModelProperty(value = "批改数")
    private Integer correctCount;

    @ApiModelProperty(value = "提交数")
    private Integer submitCount;

    @ApiModelProperty(value = "创建者用户ID")
    private Long createBy;

    @ApiModelProperty(value = "创建者姓名")
    private String createByName;

    @ApiModelProperty(value = "是否可以监考")
    private Boolean canInvigilate = false;

}

