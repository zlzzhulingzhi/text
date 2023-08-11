package cn.qbs.wa.train.allowance.pojo.apply;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * 申请表头请求参数
 *
 * @author yjx
 */
@Data
public class ApplyCommonRequest {

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "机构名称(快照)")
    private String orgName;

    @ApiModelProperty(value = "机构性质(字典值)")
    private String orgCategory;

    @ApiModelProperty(value = "机构简介")
    private String orgIntro;

    @ApiModelProperty(value = "联系人")
    private String contactPerson;

    @ApiModelProperty(value = "联系电话")
    private String contactNumber;

    @ApiModelProperty(value = "联系邮箱")
    private String contactEmail;

    @ApiModelProperty(value = "法人代表")
    private String legalPerson;

    @ApiModelProperty(value = "法人电话")
    private String legalNumber;

    @ApiModelProperty(value = "法人邮箱")
    private String legalEmail;

    @ApiModelProperty(value = "申请事由")
    private String applyReason;

    @ApiModelProperty(value = "申请备注")
    private String applyRemark;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "申请时间")
    private LocalDate applyDate;
}

