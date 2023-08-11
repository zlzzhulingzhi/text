package cn.qbs.wa.train.allowance.pojo.apply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author yjx
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QualificationApplyRequest extends ApplyCommonRequest {

    @ApiModelProperty("计划项目列表")
    private List<QualificationProjectRequest> projectList;

}
