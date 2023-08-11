package cn.qbs.wa.teach.organization.entity;


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
 * @since 2021-12-08 16:04:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrganizationConfig extends Model {

    private static final long serialVersionUID = 304908854511153194L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构Id")
    private Long orgId;

    @ApiModelProperty(value = "1-内训PC装修 2-外训PC装修 3-内训H5 4-外训H5")
    private Integer type;

    @ApiModelProperty(value = "json配置内容")
    private String text;

    @ApiModelProperty(value = "登录页面宣传图地址")
    private String loginPosterUrl;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
