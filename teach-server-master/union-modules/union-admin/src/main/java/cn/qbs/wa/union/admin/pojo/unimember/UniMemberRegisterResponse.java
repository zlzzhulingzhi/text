package cn.qbs.wa.union.admin.pojo.unimember;

import cn.qbs.wa.teach.common.security.annotation.EncodeContent;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 统一会员用户表(UniMember)统一会员用户表详情
 *
 * @author makejava
 * @version 1.0
 * @date 2022-07-21 09:11:24
 */
@Data
public class UniMemberRegisterResponse {

    @ApiModelProperty(value = "")
    private Long id;

    @EncodeContent
    @ApiModelProperty(value = "账号")
    private String account;

}

