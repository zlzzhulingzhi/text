package cn.qbs.wa.union.admin.pojo.unimember;

import cn.qbs.wa.teach.common.core.domain.BasePageSearchComDTO;
import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class UniMemberQuery extends BasePageSearchComDTO {

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "启用状态")
    private Integer enabled;

}

