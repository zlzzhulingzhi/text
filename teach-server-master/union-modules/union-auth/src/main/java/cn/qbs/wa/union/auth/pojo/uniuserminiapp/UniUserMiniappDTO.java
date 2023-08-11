package cn.qbs.wa.union.auth.pojo.uniuserminiapp;

import cn.qbs.wa.teach.common.security.annotation.EncodeContent;
import cn.qbs.wa.union.auth.entity.UniUserMiniapp;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UniUserMiniappDTO extends UniUserMiniapp {

    @EncodeContent
    @ApiModelProperty(value = "手机号")
    private String phone;
}
