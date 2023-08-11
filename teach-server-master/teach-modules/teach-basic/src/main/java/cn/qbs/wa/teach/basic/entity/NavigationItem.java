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
 * @since 2021-12-08 13:55:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NavigationItem extends Model {

    private static final long serialVersionUID = 593728648694557118L;


    @ApiModelProperty(value = "【主键标识】")
    private Long id;

    @ApiModelProperty(value = "【父导航ID】")
    private Long parentId;

    @ApiModelProperty(value = "【导航名称】")
    private String name;

    @ApiModelProperty(value = "【导航编码】")
    private String code;

    @ApiModelProperty(value = "【导航类型 1-一级导航 2-二级导航】")
    private Integer type;

    @ApiModelProperty(value = "【导航URI】")
    private String uri;

    @ApiModelProperty(value = "【导航权限 例：nav:course:list】")
    private String permission;

    @ApiModelProperty(value = "【导航排序】")
    private Integer sort;

    @ApiModelProperty(value = "【启用状态 false-0-未启用 true-1-启用】")
    private Integer enabled;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
