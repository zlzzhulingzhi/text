package cn.qbs.wa.train.allowance.pojo.apply;

import cn.qbs.wa.teach.common.security.annotation.EncodeContent;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * 申请表(Apply)分页查询申请表响应
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-14 15:40:03
 */
@Data
public class QualificationPageResponse {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "机构名称(快照)")
    private String orgName;

    @ApiModelProperty(value = "机构性质(字典值)")
    private String orgCategory;

    @ApiModelProperty(value = "法人代表")
    private String legalPerson;

    @ApiModelProperty(value = "法人电话")
    @EncodeContent
    private String legalNumber;

    @ApiModelProperty(value = "法人邮箱")
    private String legalEmail;

    @ApiModelProperty(value = "申请状态 0-未提交 1-已提交")
    private Integer applyStatus;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "申请时间")
    private LocalDate applyDate;

    @ApiModelProperty(value = "流程状态 1-审批中 2-通过 3-挂起 4-驳回 ")
    private Integer flowStatus;

    @ApiModelProperty(value = "申请结果 0-不通过 1-通过")
    private Integer applyResult;

}

