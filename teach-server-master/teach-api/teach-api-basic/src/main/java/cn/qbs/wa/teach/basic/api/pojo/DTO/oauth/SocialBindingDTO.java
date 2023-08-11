package cn.qbs.wa.teach.basic.api.pojo.DTO.oauth;

import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserInfoDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SocialBindingDTO extends UserInfoDTO {
    private String uid;
    private String nickName;
    private String headImgUrl;
    private Integer sex;
}