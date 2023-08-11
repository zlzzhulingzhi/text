package cn.qbs.wa.train.allowance.pojo.apply;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yjx
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QualificationUpdateRequest extends QualificationApplyRequest {

    private Long applyId;

}
