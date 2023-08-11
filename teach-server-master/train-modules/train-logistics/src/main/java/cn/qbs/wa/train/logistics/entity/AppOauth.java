package cn.qbs.wa.train.logistics.entity;


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
 * @since 2022-03-02 19:09:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AppOauth extends Model {

    private static final long serialVersionUID = -72678932020384281L;


    @ApiModelProperty(value = "【主键】")
    private Long id;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【用户ID，包括 员工和学员】")
    private Long userId;

    @ApiModelProperty(value = "【账号类别 employee-员工 student-学员】")
    private String accountType;

    @ApiModelProperty(value = "【第三方平台 如：weixin、weibo】")
    private String platform;

    @ApiModelProperty(value = "【第三方应用的用户唯一ID】")
    private String uid;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}
