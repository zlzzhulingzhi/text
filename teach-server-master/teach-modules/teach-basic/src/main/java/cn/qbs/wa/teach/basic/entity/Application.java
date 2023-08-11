package cn.qbs.wa.teach.basic.entity;


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
 * @since 2021-11-12 09:25:01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Application extends Model {

    private static final long serialVersionUID = -36018893330547943L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "【统一客户端类型码 如H5、PC】")
    private String clientCode;

    @ApiModelProperty(value = "【统一应用类型ID】")
    private Long appTypeId;

    @ApiModelProperty(value = "【应用图标地址】")
    private String iconUrl;

    @ApiModelProperty(value = "【应用名称】")
    private String name;

    @ApiModelProperty(value = "【应用主机】")
    private String host;

    @ApiModelProperty(value = "【应用访问地址】")
    private String uri;

    @ApiModelProperty(value = "【应用权限标识】")
    private String permission;

    @ApiModelProperty(value = "【应用备注】")
    private String remark;

    @ApiModelProperty(value = "【应用是否可用】")
    private Integer enabled;

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
