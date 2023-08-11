package cn.qbs.wa.train.basic.entity;


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
 * @since 2022-12-26 15:38:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ClockInConfig extends Model {

    private static final long serialVersionUID = -78244941261811025L;


    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "编号")
    private String siteCode;

    @ApiModelProperty(value = "打卡地点")
    private String siteName;

    @ApiModelProperty(value = "经度坐标")
    private String longitude;

    @ApiModelProperty(value = "纬度坐标")
    private String latitude;

    @ApiModelProperty(value = "打卡距离(m)")
    private Integer distance;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人ID")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
