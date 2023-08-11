package cn.qbs.wa.teach.basic.api.pojo.DTO.user;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/5 15:30
 */
@Data
public class UserOneResultDTO {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "【账号】")
    private String account;

    @ApiModelProperty(value = "【手机号】")
    private String phone;

    @ApiModelProperty(value = "【邮箱】")
    private String email;

    @ApiModelProperty(value = "【密码 若加盐就是加密后的密文】")
    private String password;

    @ApiModelProperty(value = "【盐】")
    private String salt;

    @ApiModelProperty(value = "【昵称】")
    private String nickName;

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【头像地址】")
    private String headImgUrl;

    @ApiModelProperty(value = "【性别 0-未知 1-男 2-女】")
    private Integer sex;

    @ApiModelProperty(value = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "0表示账号不可用  1表示账号可用")
    private Integer enabled;

    @ApiModelProperty(value = "身份号码")
    private String idNumber;

    @ApiModelProperty(value = "")
    private Long createBy;

    @ApiModelProperty(value = "")
    private Long updateBy;

    @ApiModelProperty("角色id数组")
    List<Long> roleIdList;
}
