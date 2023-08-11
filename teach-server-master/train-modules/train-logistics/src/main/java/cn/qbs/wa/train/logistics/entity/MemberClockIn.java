package cn.qbs.wa.train.logistics.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author makejava
 * @since 2022-12-27 14:14:22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MemberClockIn extends Model {

    private static final long serialVersionUID = -31645876736037765L;


    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "打卡目标地点编号")
    private String siteCode;

    @ApiModelProperty(value = "班级ID")
    private Long clazzId;

    @ApiModelProperty(value = "学员用户ID")
    private Long memberId;

    @ApiModelProperty(value = "学员打卡经度")
    private String longitude;

    @ApiModelProperty(value = "学员打卡纬度")
    private String latitude;

    @ApiModelProperty(value = "打卡日期")
    private LocalDate clockInDate;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "打卡时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;


}
