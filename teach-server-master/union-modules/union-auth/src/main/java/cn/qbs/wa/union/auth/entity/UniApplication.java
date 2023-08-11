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
 * @since 2022-07-08 09:03:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UniApplication extends Model {

    private static final long serialVersionUID = -22733328464417301L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "【注册ID】")
    private String applicationId;

    @ApiModelProperty(value = "【统一客户端类型码 如admin、org】")
    private String uniClientCode;

    @ApiModelProperty(value = "【统一应用类型ID】")
    private Long uniAppTypeId;

    @ApiModelProperty(value = "【应用图标地址】")
    private String appIconUrl;

    @ApiModelProperty(value = "【应用名称】")
    private String appName;

    @ApiModelProperty(value = "【应用主机】")
    private String appHost;

    @ApiModelProperty(value = "【应用访问地址】")
    private String appUri;

    @ApiModelProperty(value = "【应用权限标识】")
    private String appPermission;

    @ApiModelProperty(value = "【应用备注】")
    private String appRemark;

    @ApiModelProperty(value = "【应用是否可用】")
    private Integer enable;

    @ApiModelProperty(value = "【应用在分类中的排序】")
    private Integer sort;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
