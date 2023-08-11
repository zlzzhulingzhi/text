package cn.qbs.wa.train.allowance.pojo.applyallowance;

import cn.qbs.wa.train.allowance.pojo.apply.ApplyPageResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 资助资金申请-网络安全培训课程(ApplyAllowance)分页查询资助资金申请-网络安全培训课程响应
 *
 * @author makejava
 * @since 2022-11-03 19:28:58
 */
@Data
public class ApplyAllowancePageResponse extends ApplyPageResponse {

    @ApiModelProperty(value = "项目编号")
    private String projectNo;

    @ApiModelProperty(value = "项目名称(课程名称)")
    private String projectName;

    @ApiModelProperty(value = "合计补贴费用")
    private BigDecimal totalAllowanceFee;

    @ApiModelProperty(value = "班级名称")
    private String className;

    @ApiModelProperty(value = "申请人")
    private String applyUserName;

}

