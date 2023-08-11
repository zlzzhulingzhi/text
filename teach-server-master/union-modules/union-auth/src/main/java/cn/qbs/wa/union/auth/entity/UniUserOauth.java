package cn.qbs.wa.union.auth.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author makejava
 * @since 2022-07-12 13:53:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UniUserOauth extends Model {

    private static final long serialVersionUID = -34516507793591884L;


    @ApiModelProperty(value = "【主键】")
    private Long id;

    @ApiModelProperty(value = "【用户ID】")
    private Long userId;

    @ApiModelProperty(value = "【应用ID】")
    private Long appId;

    @ApiModelProperty(value = "【授权第三方应用 如：weixin、weibo】")
    private String oauthApp;

    @ApiModelProperty(value = "【授权第三方应用的用户唯一ID】")
    private String uid;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}
