package cn.qbs.wa.train.allowance.pojo.apply;

import cn.qbs.wa.train.allowance.entity.Apply;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author yjx
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QualificationApplyResponse extends Apply {

    @ApiModelProperty(value = "资质申请主键")
    private Long qualificationId;

    @ApiModelProperty("计划项目列表")
    private List<QualificationProjectResponse> projectList;

}
