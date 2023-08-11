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
 * @since 2022-12-28 16:24:18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MemberVisit extends Model {

    private static final long serialVersionUID = 854641250904651066L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "访客名称")
    private String visitorName;

    @ApiModelProperty(value = "学员用户ID")
    private Long memberId;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "车牌号")
    private String carNumber;

    @ApiModelProperty(value = "访问人物")
    private String visitPeople;

    @ApiModelProperty(value = "访问部门")
    private String visitDepartment;

    @ApiModelProperty(value = "访问原因")
    private String visitReason;

    @ApiModelProperty(value = "访问时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime visitTime;

    @ApiModelProperty(value = "审核用户Id")
    private Long auditBy;

    @ApiModelProperty(value = "审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime auditTime;

    @ApiModelProperty(value = "审核状态(0 未通过 1已通过)")
    private Integer auditStats;

    @ApiModelProperty(value = "审核结果")
    private String auditResult;

    @ApiModelProperty(value = "0 作废 1启用")
    private Integer enabled;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "访问机构ID")
    private Long visitOrgId;

}
