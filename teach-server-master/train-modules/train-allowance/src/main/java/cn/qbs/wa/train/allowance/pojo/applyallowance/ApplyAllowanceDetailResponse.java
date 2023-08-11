package cn.qbs.wa.train.allowance.pojo.applyallowance;

import cn.qbs.wa.train.allowance.entity.ApplyAllowance;
import cn.qbs.wa.train.allowance.pojo.applyallowancefee.ApplyAllowanceFeeDetailResponse;
import cn.qbs.wa.train.allowance.pojo.applyallowancestudent.ApplyAllowanceStudentDetailResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 资助资金申请-网络安全培训课程(ApplyAllowance)资助资金申请-网络安全培训课程详情
 *
 * @author makejava
 * @since 2022-11-03 19:28:58
 */
@Data
public class ApplyAllowanceDetailResponse extends ApplyAllowance {

    @ApiModelProperty(value = "课程学生集合")
    private List<ApplyAllowanceStudentDetailResponse> applyAllowanceStudentDetailResponseList;

    @ApiModelProperty(value = "课程费用集合")
    private List<ApplyAllowanceFeeDetailResponse> applyAllowanceFeeDetailResponses;
}

