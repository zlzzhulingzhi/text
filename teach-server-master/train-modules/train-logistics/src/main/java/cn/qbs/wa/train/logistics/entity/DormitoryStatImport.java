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
 * @since 2023-06-05 10:59:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DormitoryStatImport extends Model {

    private static final long serialVersionUID = -18943965427981562L;


    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate day;

    @ApiModelProperty(value = "描述")
    private String descr;

    @ApiModelProperty(value = "总量")
    private String totals;

    @ApiModelProperty(value = "总量含超预留")
    private String exceedTotals;

    @ApiModelProperty(value = "非确认")
    private String unconfirmed;

    @ApiModelProperty(value = "出租率")
    private String occupancyRate;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}
