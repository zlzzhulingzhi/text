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
 * @since 2022-07-08 09:03:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UniApplicationUser extends Model {

    private static final long serialVersionUID = -69530184569006780L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "【系统用户ID】")
    private Long systemUserId;

    @ApiModelProperty(value = "【统一应用ID】")
    private Long uniAppId;

    @ApiModelProperty(value = "【应用在常用应用中的排序】")
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
