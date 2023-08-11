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
import java.util.Date;

/**
 * @author makejava
 * @since 2022-10-18 14:10:54
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DormitorySchedule extends Model {

    private static final long serialVersionUID = -29445983113979500L;


    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "宿舍ID")
    private Long dormitoryId;

    @ApiModelProperty(value = "使用日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate useDate;

    @ApiModelProperty(value = "使用状态(0-未使用，1-已使用)")
    private String useState;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改人ID")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "机构名称(快照)")
    private String orgName;

    @ApiModelProperty(value = "申请ID")
    private Long applyId;

    @ApiModelProperty(value = "宿舍单元(字典值)")
    private String building;

    @ApiModelProperty(value = "楼层(字典值)")
    private String floor;

    @ApiModelProperty(value = "房号")
    private String roomNo;

    @ApiModelProperty(value = "房型(字典值)")
    private String roomType;

}
