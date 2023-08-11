package cn.qbs.wa.train.allowance.pojo.stat;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrgAllowanceResponse extends SummaryAllowanceResponse {

    private Long orgId;

    private String orgName;

    private String applyType;

}
