package cn.qbs.wa.teach.basic.pojo.user;

import cn.qbs.wa.teach.common.security.annotation.EncodeContent;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/4 19:29
 */
@Data
public class UserPageResponse {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "【账号】")
    @EncodeContent
    private String account;

    @ApiModelProperty(value = "【手机号】")
    @EncodeContent
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
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "0表示账号不可用  1表示账号可用")
    private Integer enabled;

    @ApiModelProperty(value = "身份号码")
    @EncodeContent
    private String idNumber;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty("角色名称")
    String roleName;

    @ApiModelProperty("角色Code")
    String roleCode;

    @ApiModelProperty(value = "权重")
    private Integer priority;

    @ApiModelProperty(value = "编辑状态 0 不可编辑 1可编辑")
    private Boolean editable;

}
