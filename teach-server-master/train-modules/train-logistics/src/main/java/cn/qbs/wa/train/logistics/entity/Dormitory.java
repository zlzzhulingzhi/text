package cn.qbs.wa.train.logistics.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author makejava
 * @since 2022-10-08 17:39:59
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Dormitory extends Model {

    private static final long serialVersionUID = 346982436796092422L;


    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "宿舍单元(字典值)")
    private String building;

    @ApiModelProperty(value = "楼层(字典值)")
    private String floor;

    @ApiModelProperty(value = "房号")
    private String roomNo;

    @ApiModelProperty(value = "房型(字典值)")
    private String roomType;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "启用状态(0-不可用  1-可用)")
    private Integer enabled;

    @TableLogic(value = "0", delval = "1")
    @ApiModelProperty(value = "删除状态 0-正常 1-已删除")
    private Integer deleted;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "最后修改人ID")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "最后修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
